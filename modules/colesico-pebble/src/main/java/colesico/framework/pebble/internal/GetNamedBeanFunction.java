package colesico.framework.pebble.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.NamedKey;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

public class GetNamedBeanFunction implements Function {
    public static final String FUNCTION_NAME = "getNamedBean";

    private final Ioc ioc;

    public GetNamedBeanFunction(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate pebbleTemplate, EvaluationContext context, int lineNumber) {
        String className = (String) args.get("0");
        String name = (String) args.get("1");
        try {
            Class<?> instanceClass = Class.forName(className);
            return ioc.instance(new NamedKey(instanceClass, name), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }

}
