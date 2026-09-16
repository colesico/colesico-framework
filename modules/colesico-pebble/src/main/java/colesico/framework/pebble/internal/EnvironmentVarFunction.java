package colesico.framework.pebble.internal;

import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Return given environment variable or test it value
 */
public class EnvironmentVarFunction implements Function {
    public static final String FUNCTION_NAME = "env";

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        var name = (String) args.get("0");
        var expectedValue = (String) args.get("1");

        if (expectedValue == null) {
            return System.getenv(name);
        } else {
            return Boolean.valueOf(expectedValue.equals(System.getenv(name)));
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
