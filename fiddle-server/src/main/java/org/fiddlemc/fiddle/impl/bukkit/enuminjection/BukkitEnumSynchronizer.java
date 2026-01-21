package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import org.fiddlemc.fiddle.impl.java.enuminjection.EnumInjector;
import java.util.List;

/**
 * An abstract base for a synchronizer that injects new values into a Bukkit enum {@link E}
 * that correspond to values from another source (of type {@link T}).
 *
 * <p>
 * To apply the synchronization, call {@link #run}.
 * </p>
 */
public abstract class BukkitEnumSynchronizer<E extends Enum<E>, T, I extends EnumInjector<E>> {

    protected final I injector;

    public BukkitEnumSynchronizer(I injector) {
        this.injector = injector;
    }

    /**
     * @return The source values for the injected new values.
     */
    protected abstract List<T> getSourceValues();

    /**
     * Stages an enum value for the given name and source value.
     *
     * @param enumName    The {@link Enum#name()} for the new value.
     * @param sourceValue The source value for which this new value is being injected.
     */
    protected abstract void stage(String enumName, T sourceValue);

    /**
     * @param sourceValue A source value.
     * @return The desired {@link Enum#name()} for the corresponding new value.
     * While not generally enforced, this should only consist of uppercase letters, digits and underscores.
     * @throws EnumNameMappingException When the source value cannot be mapped to an acceptable enum name.
     */
    protected abstract String getEnumName(T sourceValue) throws EnumNameMappingException;

    /**
     * @param enumName A string being considered for an enum value's {@link Enum#name()}.
     * @throws EnumNameMappingException If the given string is not an acceptable enum name.
     */
    protected void checkAcceptableEnumName(String enumName) throws EnumNameMappingException {
        boolean hasNonUnderscore = false;
        for (char c : enumName.toCharArray()) {
            if (c == '_') {
                // Underscores are allowed
                continue;
            }
            hasNonUnderscore = true;
            if ((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                // Uppercase alphanumeric characters are allowed
                continue;
            }
            throw new EnumNameMappingException("An enum name (" + enumName + ") contains a " + c + " character, which is not allowed by Fiddle");
        }
        if (!hasNonUnderscore) {
            throw new EnumNameMappingException("An enum name (" + enumName + ") contains only underscores, which is not allowed by Java");
        }
        // The enum name is acceptable
    }

    /**
     * Adds the new values into the enum.
     *
     * @throws EnumNameMappingException If a source value cannot be mapped to an acceptable enum name.
     * @throws Exception                If something goes wrong.
     */
    public void run() throws EnumNameMappingException, Exception {

        // Get the source values
        List<T> sourceValues = this.getSourceValues();

        // Skip if there is nothing to synchronize
        if (sourceValues.isEmpty()) {
            return;
        }

        // Stage the new values
        for (T sourceValue : sourceValues) {
            // Determine the enum name
            String enumName = this.getEnumName(sourceValue);
            // Verify sure the resulting enum name is valid
            this.checkAcceptableEnumName(enumName);
            // Stage the new value
            this.stage(enumName, sourceValue);
        }

        // Commit the new values
        this.injector.commit();

    }

    /**
     * An exception that can be thrown when mapping to an acceptable enum name is not possible.
     */
    public static class EnumNameMappingException extends Exception {

        EnumNameMappingException(String message) {
            super(message);
        }

    }

}
