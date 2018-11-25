package colesico.framework.translation.internal;

import colesico.framework.translation.Translatable;
import colesico.framework.translation.TranslationKit;

public class TranslatableImpl implements Translatable {
    private final TranslationKit translationKit;
    private final String basePath;
    private final String key;

    public TranslatableImpl(TranslationKit translationKit, String basePath, String key) {
        this.translationKit = translationKit;
        this.basePath = basePath;
        this.key = key;
    }

    @Override
    public String translate(Object... params) {
        return translationKit.getBundle(basePath).get(key,key, params);
    }
}
