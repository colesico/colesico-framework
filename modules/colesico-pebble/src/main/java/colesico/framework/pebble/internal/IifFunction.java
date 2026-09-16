package colesico.framework.pebble.internal;

import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;


/**
 * A Pebble template extension function that mimics the SQL {@code IIF} behavior.
 * It evaluates a boolean condition and returns one of two values based on the result.
 * <p><b>Usage in template:</b> {@code {{ iif(condition, trueValue, falseValue) }}}</p>
 */
public class IifFunction implements Function {
    public static final String FUNCTION_NAME = "iif";

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        Boolean condition = (Boolean) args.get("0");
        var trueValue = args.get("1");
        var falseValue = args.get("2");
        return condition ? trueValue : falseValue;
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
