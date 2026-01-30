package org.fiddlemc.fiddle.impl.packetmapping.component.translatable;

import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationRegistrarComposeEvent;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The implementation of {@link ServerSideTranslationRegistrar}.
 */
public final class ServerSideTranslationRegistrarImpl extends ComposableImpl<ServerSideTranslationRegistrarComposeEvent, ServerSideTranslationRegistrarComposeEventImpl> implements ServerSideTranslationRegistrar {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ServerSideTranslationRegistrar, ServerSideTranslationRegistrarImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(ServerSideTranslationRegistrarImpl.class);
        }

    }

    public static ServerSideTranslationRegistrarImpl get() {
        return (ServerSideTranslationRegistrarImpl) ServerSideTranslationRegistrar.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_server_side_translation_registrar";
    }

    @Override
    protected ServerSideTranslationRegistrarComposeEventImpl createComposeEvent() {
        return new ServerSideTranslationRegistrarComposeEventImpl(this);
    }

    /**
     * A map of the registered translations per key.
     */
    final Map<String, RegisteredTranslationsForKey> registeredTranslations = new HashMap<>();

    static final class RegisteredTranslationsForKey {

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
