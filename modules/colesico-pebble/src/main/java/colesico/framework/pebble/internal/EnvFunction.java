package colesico.framework.pebble.internal;

import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Return  environment variable or default value
 */
public class EnvFunction implements Function {
    public static final String FUNCTION_NAME = "env";

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        var name = (String) args.get("0");
        var defaultValue = (String) args.get("1");

        var value = System.getenv(name);
        
        if (value == null) {
            return defaultValue ;
        }

        return value;
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
