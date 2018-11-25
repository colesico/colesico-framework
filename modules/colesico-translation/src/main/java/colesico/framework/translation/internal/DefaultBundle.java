package colesico.framework.translation.internal;

import colesico.framework.translation.Bundle;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

public final class DefaultBundle implements Bundle {

    // Translations map  key->translation
    private final Properties translations;

    public DefaultBundle(Properties translations) {
        this.translations = translations;
    }

    @Override
    public String get(final String key, final String defaultVal, final Object... params) {
        final String translation = translations.getProperty(key);

        if (translation == null) {
            return defaultVal;
        }

        if (params.length > 0) {
            return MessageFormat.format(translation, params);
        }

        return translation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(DefaultBundle.class.getName()).append(":\n");
        for (Map.Entry<Object, Object> me : translations.entrySet()) {
            sb.append(me.getKey()).append('=').append(me.getValue()).append(";\n");
        }
        return sb.toString();
    }
}
