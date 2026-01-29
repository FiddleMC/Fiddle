package org.fiddlemc.fiddle.impl.packetmapping.component.translatable;

import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationRegistrar;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The implementation of {@link ServerSideTranslationRegistrar}.
 */
public final class ServerSideTranslationRegistrarImpl implements ServerSideTranslationRegistrar {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ServerSideTranslationRegistrar, ServerSideTranslationRegistrarImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(ServerSideTranslationRegistrarImpl.class);
        }

    }

    public static ServerSideTranslationRegistrarImpl get() {
        return (ServerSideTranslationRegistrarImpl) ServerSideTranslationRegistrar.get();
    }

    /**
     * A map of the registered translations per key.
     */
    private final Map<String, RegisteredTranslationsForKey> registeredTranslations = new HashMap<>();

    private static class RegisteredTranslationsForKey {

        /**
         * A translation that can act as the fallback for any locale,
         * or null if none is registered.
         */
        @Nullable ServerSideTranslation genericTranslation;

        /**
         * Translations that can act as the fallback for specific language groups,
         * indexed by their lower-case language group,
         * or null if none are registered.
         */
        @Nullable Map<String, ServerSideTranslation> languageGroupTranslations;

        /**
         * Translations for specific locales,
         * indexed by their lower-case locale.
         */
        Map<String, ServerSideTranslation> localeTranslations = new HashMap<>(2);

    }

    @Override
    public void register(final String key, final String translation, final String locale, final FallbackScope fallbackScope, boolean overrideClientSide) {

        // Do some basic checks for the arguments
        String lowerCaseKey = key.toLowerCase(Locale.ROOT);
        if (!key.equals(lowerCaseKey)) {
            throw new IllegalArgumentException("The given key (" + key + ") is not lower-case");
        }
        String trimmedKey = key.trim();
        if (!key.equals(trimmedKey)) {
            throw new IllegalArgumentException("The given key (" + key + ") includes whitespace");
        }
        String lowerCaseLocale = locale.toLowerCase(Locale.ROOT);
        if (!locale.equals(lowerCaseLocale)) {
            throw new IllegalArgumentException("The given locale (" + locale + ") is not lower-case");
        }
        String trimmedLocale = locale.trim();
        if (!locale.equals(trimmedLocale)) {
            throw new IllegalArgumentException("The given locale (" + locale + ") includes whitespace");
        }

        // Add the translations
        RegisteredTranslationsForKey translationsForKey = registeredTranslations.computeIfAbsent(key, $ -> new RegisteredTranslationsForKey());
        ServerSideTranslation translationToRegister = new ServerSideTranslation(translation, overrideClientSide);
        if (fallbackScope.equals(FallbackScope.ALL)) {
            // Add as generic fallback
            if (translationsForKey.genericTranslation == null || overrideClientSide || !translationsForKey.genericTranslation.overrideClientSide()) {
                translationsForKey.genericTranslation = translationToRegister;
            }
        }
        if (!fallbackScope.equals(FallbackScope.LOCALE)) {
            // Add as group fallback
            int underscoreIndex = locale.indexOf('_');
            if (underscoreIndex > 0) {
                String group = locale.substring(0, underscoreIndex);
                if (translationsForKey.languageGroupTranslations == null) {
                    translationsForKey.languageGroupTranslations = new HashMap<>(2);
                    translationsForKey.languageGroupTranslations.put(group, translationToRegister);
                } else {
                    translationsForKey.languageGroupTranslations.compute(group, ($, existingTranslation) -> {
                        if (existingTranslation == null || overrideClientSide || !existingTranslation.overrideClientSide()) {
                            return translationToRegister;
                        }
                        return existingTranslation;
                    });
                }
            }
        }
        translationsForKey.localeTranslations.compute(locale, ($, existingTranslation) -> {
            if (existingTranslation == null || overrideClientSide || !existingTranslation.overrideClientSide()) {
                return translationToRegister;
            }
            return existingTranslation;
        });

    }

    @Override
    public @Nullable ServerSideTranslation get(final String key, final @Nullable String locale) {
        String lowerCaseKey = key.toLowerCase(Locale.ROOT);
        @Nullable String lowerCaseLocale = locale == null ? null : locale.toLowerCase(Locale.ROOT);
        @Nullable RegisteredTranslationsForKey translationsForKey = registeredTranslations.get(lowerCaseKey);
        if (translationsForKey == null) {
            return null;
        }
        @Nullable ServerSideTranslation translation = null;
        if (lowerCaseLocale != null) {
            translation = translationsForKey.localeTranslations == null ? null : translationsForKey.localeTranslations.get(lowerCaseLocale);
            if (translation != null && translation.overrideClientSide()) {
                return translation;
            }
            int underscoreIndex = lowerCaseLocale.indexOf('_');
            if (underscoreIndex > 0) {
                String group = lowerCaseLocale.substring(0, underscoreIndex);
                @Nullable ServerSideTranslation alternative = translationsForKey.languageGroupTranslations == null ? null : translationsForKey.languageGroupTranslations.get(group);
                if (alternative != null) {
                    if (alternative.overrideClientSide()) {
                        return alternative;
                    }
                    if (translation == null) {
                        translation = alternative;
                    }
                }
            }
        }
        @Nullable ServerSideTranslation alternative = translationsForKey.genericTranslation;
        if (alternative != null) {
            if (alternative.overrideClientSide()) {
                return alternative;
            }
            if (translation == null) {
                translation = alternative;
            }
        }
        return translation;
    }

}
