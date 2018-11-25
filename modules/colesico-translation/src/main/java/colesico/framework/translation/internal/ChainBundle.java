package colesico.framework.translation.internal;

import colesico.framework.assist.LazySingleton;
import colesico.framework.translation.Bundle;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

public final class ChainBundle implements Bundle {

    // Translations map  key->translation
    private final Properties translations;

    private final LazySingleton<Bundle> next;

    public ChainBundle(final Properties translations, final String basePath, final TranslationKitImpl translationKit) {
        this.translations = translations;
        this.next = new LazySingleton<>() {
            @Override
            public Bundle create() {
                return translationKit.getBundle(basePath, false);
            }
        };
    }

    @Override
    public String get(final String key, final String defaultVal, final Object... params) {
        final String translation = translations.getProperty(key);

        if (translation == null) {
            return next.get().get(key, defaultVal, params);
        }

        if (params.length > 0) {
            return MessageFormat.format(translation, params);
        }

        return translation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Object, Object> me : translations.entrySet()) {
            sb.append(me.getKey()).append('=').append(me.getValue()).append(";\n");
        }
        return sb.toString();
    }
}
