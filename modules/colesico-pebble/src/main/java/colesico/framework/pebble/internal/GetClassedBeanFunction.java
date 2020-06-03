package colesico.framework.pebble.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.NamedKey;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

public class GetClassedBeanFunction implements Function {
    public static final String FUNCTION_NAME = "getClassedBean";

    private final Ioc ioc;

    public GetClassedBeanFunction(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate pebbleTemplate, EvaluationContext context, int lineNumber) {
        String className = (String) args.get("0");
        String classifier = (String) args.get("1");
        try {
            Class<?> instanceClass = Class.forName(className);
            Class<?> classifierClass = Class.forName(classifier);
            return ioc.instance(new ClassedKey(instanceClass, classifierClass), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }

}
