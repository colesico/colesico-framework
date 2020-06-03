package colesico.framework.pebble.internal;

import colesico.framework.ioc.Ioc;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

public class GetBeanFunction implements Function {
    public static final String FUNCTION_NAME = "getBean";

    private final Ioc ioc;

    public GetBeanFunction(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate pebbleTemplate, EvaluationContext context, int lineNumber) {
        String className = (String) args.get("0");
        try {
            Class instanceClass = Class.forName(className);
            return ioc.instance(instanceClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }

}
