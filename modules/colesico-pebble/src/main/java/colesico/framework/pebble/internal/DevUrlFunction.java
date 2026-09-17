package colesico.framework.pebble.internal;

import colesico.framework.assist.StringUtils;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Return  environment variable or default value
 */
public class DevUrlFunction implements Function {
    public static final String FUNCTION_NAME = "devUrl";

    /**
     * UI development url env variable name
     */
    public static String UI_DEV_SERVER_ENV = "UI_DEV_SERVER";

    /**
     * UI development url  system property name
     */
    public static String UI_DEV_URL_ARG = "uiDevServer";


    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        var devUrl = (String) args.get("0");
        if (StringUtils.isBlank(devUrl)) {
            devUrl = "";
        }

        var devServer = System.getenv(UI_DEV_SERVER_ENV);
        if (StringUtils.isBlank(devServer)) {
            devServer = System.getProperty(UI_DEV_URL_ARG);
        }

        if (StringUtils.isBlank(devServer)) {
            return devUrl;
        } else {
            return devServer + devUrl;
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
