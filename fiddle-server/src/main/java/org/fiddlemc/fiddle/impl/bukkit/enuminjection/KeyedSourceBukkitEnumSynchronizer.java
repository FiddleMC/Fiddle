package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.fiddlemc.fiddle.impl.java.enuminjection.EnumInjector;

/**
 * A {@link BukkitEnumSynchronizer} for which the source value has a {@link NamespacedKey},
 * and which bases the enum name on that key.
 */
public abstract class KeyedSourceBukkitEnumSynchronizer<E extends Enum<E>, T, I extends EnumInjector<E>> extends BukkitEnumSynchronizer<E, T, I> {

    public KeyedSourceBukkitEnumSynchronizer(I injector) {
        super(injector);
    }

    /**
     * @return The {@link NamespacedKey} for the given source value.
     */
    protected abstract NamespacedKey getKey(T sourceValue);

    @Override
    protected String getEnumName(T sourceValue) throws EnumNameMappingException {
        // Get the key
        NamespacedKey key = this.getKey(sourceValue);
        // Check if the key is acceptable
        this.checkAcceptableNamespacedKey(key);
        // Map the key to the enum name
        return "FIDDLE_" + this.mapNamespacedKeyPartToEnumPart(key.namespace()) + "_" + this.mapNamespacedKeyPartToEnumPart(key.getKey());
    }

    /**
     * @param key  A {@link NamespacedKey} to be mapped to an enum name.
     * @throws EnumNameMappingException If the given {@code key} cannot be mapped to an acceptable enum name.
     */
    protected void checkAcceptableNamespacedKey(NamespacedKey key) throws EnumNameMappingException {
        this.checkAcceptableNamespacedKeyPart(key, key.namespace());
        this.checkAcceptableNamespacedKeyPart(key, key.getKey());
    }

    /**
     * @param key  A {@link NamespacedKey} to be mapped to an enum name.
     * @param part A part of the {@code key}: the namespace or the path.
     * @throws EnumNameMappingException If the given {@code part} makes it impossible to map
     *                                  the given {@code key} to an acceptable enum name.
     */
    protected void checkAcceptableNamespacedKeyPart(NamespacedKey key, String part) throws EnumNameMappingException {
        for (char c : part.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                // Alphanumeric characters are allowed
                continue;
            }
            if (c == '_') {
                // Underscores are allowed
                continue;
            }
            if (c == '.' || c == '-' || c == '/') {
                throw new EnumNameMappingException("A block or item key (" + key + ") contains a " + c + " character, which is technically allowed by Minecraft, but not by Fiddle");
            }
            throw new EnumNameMappingException("A block or item key (" + key + ") contains a " + c + " character, which is not allowed by Fiddle");
        }
        // The part is acceptable
    }

    /**
     * Maps a {@link NamespacedKey} namespaced or path to a fitting enum name part.
     *
     * <p>
     * This method assumes that the part is {@linkplain #checkAcceptableNamespacedKeyPart acceptable}.
     * </p>
     *
     * @return The enum name part.
     */
    protected String mapNamespacedKeyPartToEnumPart(String part) {
        return part.toUpperCase(Locale.ROOT);
    }

}
