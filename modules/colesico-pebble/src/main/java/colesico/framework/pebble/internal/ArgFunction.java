package colesico.framework.pebble.internal;

import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Return system property or default value
 */
public class ArgFunction implements Function {
    public static final String FUNCTION_NAME = "arg";

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        var name = (String) args.get("0");
        var defaultValue = (String) args.get("1");

        var value = System.getProperty(name);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
