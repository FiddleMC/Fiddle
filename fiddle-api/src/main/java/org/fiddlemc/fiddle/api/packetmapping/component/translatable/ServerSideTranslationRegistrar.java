package org.fiddlemc.fiddle.api.packetmapping.component.translatable;

import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;
import org.jspecify.annotations.Nullable;
import java.util.ServiceLoader;

/**
 * Provides functionality to register server-side translations.
 *
 * <p>
 * This can also be used to override existing translations.
 * </p>
 */
public interface ServerSideTranslationRegistrar {

    /**
     * An internal interface to get the {@link ServerSideTranslationRegistrar} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<ServerSideTranslationRegistrar> {
    }

    /**
     * @return The {@link ServerSideTranslationRegistrar} instance.
     */
    static ServerSideTranslationRegistrar get() {
        return ServiceLoader.load(ServerSideTranslationRegistrar.ServiceProvider.class, ServerSideTranslationRegistrar.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

    /**
     * An extent to which a registered translation can be used as a fallback.
     */
    enum FallbackScope {
        /**
         * The translation is only used for the specific locale.
         *
         * <p>
         * For example, when used with the locale {@code en_gb}, the translation with only apply to users who
         * have the locale {@code en_gb}.
         * </p>
         */
        LOCALE,
        /**
         * The translation is used for the specific locale,
         * but can also be used for other locales of the same language group if no {@link #LOCALE}
         * translation is available for that locale.
         *
         * <p>
         * For example, when used with the locale {@code en_gb}, the translation will apply to users who
         * have the locale {@code en_gb}, but also to any users who have a different {@code en_} locale,
         * such as {@code en_us} (unless a more specific translation has been registered).
         * </p>
         */
        LANGUAGE_GROUP,
        /**
         * The translation is used for the specific locale,
         * but can also be used for any other locale if no {@link #LOCALE} or {@link #LANGUAGE_GROUP} translation is available
         * for that locale.
         *
         * <p>
         * For example, when used with the locale {@code en_gb}, the translation will apply to users who
         * have the locale {@code en_gb}, but also to any users who have another locale, such as {@code de_de}
         * (unless a more specific translation has been registered).
         * </p>
         */
        ALL
    }

    /**
     * The same as {@link #register(String, String, String, FallbackScope, boolean)},
     * but with:
     * <ul>
     *     <li>{@code locale} = {@code en_us}</li>
     *     <li>{@code fallbackScope} = {@link FallbackScope#ALL}</li>
     *     <li>{@code overrideClientSide} = {@code true}</li>
     * </ul>
     */
    default void register(String key, String translation) {
        this.register(key, translation, "en_us", FallbackScope.ALL, true);
    }

    /**
     * Register a new translation.
     *
     * <p>
     * Translations that are registered later
     * replace translations that are registered with the same parameters before.
     * </p>
     *
     * <p>
     * Translations can also replace Minecraft vanilla translations,
     * such as for the key "{@code block.minecraft.bookshelf}".
     * </p>
     *
     * @param key                The lower-case key, for example "{@code item.example.ash}".
     * @param translation        The desired translation, for example "{@code 灰}".
     * @param locale             A lower-case locale that exists in Minecraft,
     *                           for example "{@code ja_jp}" for Japanese.
     * @param fallbackScope      The extent to which the translation can serve as a fallback.
     * @param overrideClientSide Whether this translation also overrides translations already registered
     *                           on the client (such as vanilla names for Minecraft blocks and items).
     */
    void register(String key, String translation, String locale, FallbackScope fallbackScope, boolean overrideClientSide);

    /**
     * A registered server-side translation.
     */
    record ServerSideTranslation(String translation, boolean overrideClientSide) {
    }

    /**
     * @return The best registered server-side translation for the given key and locale,
     * or null if none could be found.
     */
    @Nullable ServerSideTranslation get(String key, @Nullable String locale);

}
