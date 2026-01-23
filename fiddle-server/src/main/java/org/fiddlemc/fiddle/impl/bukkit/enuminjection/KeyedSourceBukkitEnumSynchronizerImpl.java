package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import java.util.Locale;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import org.bukkit.NamespacedKey;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.KeyedSourceBukkitEnumSynchronizer;
import org.fiddlemc.fiddle.impl.java.enuminjection.EnumInjector;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of {@link KeyedSourceBukkitEnumSynchronizer}.
 */
public abstract class KeyedSourceBukkitEnumSynchronizerImpl<E extends Enum<E>, T, I extends EnumInjector<E>> extends BukkitEnumSynchronizerImpl<E, T, I> implements KeyedSourceBukkitEnumSynchronizer<E, T> {

    public static final String DEFAULT_ENUM_PREFIX = "FIDDLE_";

    /**
     * @return The {@link NamespacedKey} for the given source value.
     */
    protected abstract NamespacedKey getKey(T sourceValue);

    @Override
    protected String determineEnumName(T sourceValue) throws EnumNameMappingException {
        // Get the key
        NamespacedKey key = this.getKey(sourceValue);
        // Check if the key is acceptable
        this.checkAcceptableNamespacedKey(key);
        // Map the key to the enum name
        String originalEnumName = DEFAULT_ENUM_PREFIX + this.mapNamespacedKeyPartToEnumPart(key.namespace()) + "_" + this.mapNamespacedKeyPartToEnumPart(key.getKey());
        // Fire the event
        DetermineEnumNameEventImpl<T> event = new DetermineEnumNameEventImpl<>(sourceValue, originalEnumName);
        LifecycleEventRunner.INSTANCE.callEvent(this.determineEnumNameEventType(), event);
        // Return the final enum name
        return event.enumName;
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
        if (part.isEmpty()) {
            throw new EnumNameMappingException("A key (" + key + ") contains an empty part, which is not allowed by Minecraft");
        }
        boolean hasNonUnderscore = false;
        for (char c : part.toCharArray()) {
            if (c == '_') {
                // Underscores are allowed
                continue;
            }
            hasNonUnderscore = true;
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                // Lowercase alphanumeric characters are allowed
                continue;
            }
            if (c == '.' || c == '-' || c == '/') {
                throw new EnumNameMappingException("A key (" + key + ") contains a " + c + " character, which is technically allowed by Minecraft, but not by Fiddle");
            }
            throw new EnumNameMappingException("A key (" + key + ") contains a " + c + " character, which is not allowed by Minecraft");
        }
        if (!hasNonUnderscore) {
            throw new EnumNameMappingException("A key (" + key + ") contains a part with only underscores, which is technically allowed by Minecraft, but not by Fiddle");
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

    public static final class DetermineEnumNameEventImpl<T> implements DetermineEnumNameEvent<T>, PaperLifecycleEvent {

        public final T sourceValue;
        public final String originalEnumName;
        private String enumName;

        public DetermineEnumNameEventImpl(T sourceValue, String originalEnumName) {
            this.sourceValue = sourceValue;
            this.originalEnumName = originalEnumName;
            this.enumName = originalEnumName;
        }

        @Override
        public T getSourceValue() {
            return this.sourceValue;
        }

        @Override
        public String getOriginallyDeterminedEnumName() {
            return this.originalEnumName;
        }

        @Override
        public String getDeterminedEnumName() {
            return this.enumName;
        }

        @Override
        public void setDeterminedEnumName(String enumName) {
            this.enumName = enumName;
        }

    }

    /**
     * The prefix for the {@link LifecycleEventType#name()} of events for this synchronizer.
     */
    protected abstract String getEventTypeNamePrefix();

    public final class DetermineEnumNameEventType extends PrioritizableLifecycleEventType.Simple<BootstrapContext, DetermineEnumNameEvent<T>> {

        private DetermineEnumNameEventType() {
            super(KeyedSourceBukkitEnumSynchronizerImpl.this.getEventTypeNamePrefix() + "/determine_enum_name", BootstrapContext.class);
        }

    }

    /**
     * The cached return value of {@link #determineEnumNameEventType()},
     * or null if not cached yet.
     */
    private @Nullable DetermineEnumNameEventType determineEnumNameEventType;

    @Override
    public DetermineEnumNameEventType determineEnumNameEventType() {
        if (this.determineEnumNameEventType == null) {
            this.determineEnumNameEventType = new DetermineEnumNameEventType();
        }
        return this.determineEnumNameEventType;
    }

}
