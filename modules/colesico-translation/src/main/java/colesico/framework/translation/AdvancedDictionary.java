package colesico.framework.translation;

public interface AdvancedDictionary {

    String TRANSLATE_METHOD = "translate";
    String GET_BUNDLE_METHOD = "getBundle";
    String GET_BASE_PATH_METHOD = "getBasePath";

    String translate(String key, String defaultValue, Object... params);

    Bundle getBundle();

    String getBasePath();
}
