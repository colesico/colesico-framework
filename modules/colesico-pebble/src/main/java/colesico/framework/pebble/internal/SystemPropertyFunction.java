package colesico.framework.pebble.internal;

import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Return given system property variable or test it value
 */
public class SystemPropertyFunction implements Function {
    public static final String FUNCTION_NAME = "systemProperty";

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        var name = (String) args.get("0");
        var expectedValue = (String) args.get("1");

        if (expectedValue == null) {
            return System.getProperty(name);
        } else {
            return Boolean.valueOf(expectedValue.equals(System.getProperty(name)));
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
