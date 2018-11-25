package colesico.framework.translation;

abstract public class AbstractDictionary implements AdvancedDictionary {

    public static final String TRANSLATE_OR_KEY_METHOD = "translateOrKey";
    public static final String TRANSLATION_KIT_FIELD = "translationKit";
    public static final String BASE_PATH_FIELD = "basePath";

    protected final String basePath;
    protected final TranslationKit translationKit;

    public AbstractDictionary(TranslationKit translationKit, String basePath) {
        this.basePath = basePath;
        this.translationKit = translationKit;
    }

    public final String getBasePath() {
        return basePath;
    }

    protected final String translateOrKey(final String key, Object... params) {
        return translationKit.getBundle(basePath).get(key, key, params);
    }

    @Override
    public final String translate(final String key, final String defaultValue, Object... params) {
        return translationKit.getBundle(basePath).get(key, defaultValue, params);
    }

    @Override
    public final Bundle getBundle() {
        return translationKit.getBundle(basePath);
    }
}
