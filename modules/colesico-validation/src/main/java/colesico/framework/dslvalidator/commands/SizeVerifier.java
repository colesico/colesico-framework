package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

import java.util.Collection;
import java.util.Map;

public class SizeVerifier<V> extends AbstractIntervalVerifier<V> {

    protected final ValidatorMessages msg;

    public SizeVerifier(Number min, Number max, boolean includeEndpoints, ValidatorMessages msg) {
        super(min, max, includeEndpoints);
        this.msg = msg;
    }


    @Override
    protected String valueShouldBeBetween(Number min, Number max) {
        return null;
    }

    @Override
    protected String valueShouldBeGreaterThan(Number min) {
        return null;
    }

    @Override
    protected String valueShouldBeLessThan(Number max) {
        return null;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        Object value = context.getValue();
        Number size;

        if (value == null) {
            size = 0;
        } else if (value instanceof Collection) {
            size = ((Collection) value).size();
        } else if (value instanceof Map) {
            size = ((Map) value).size();
        } else if (value.getClass().isArray()) {
            size = ((Object[]) context.getValue()).length;
        } else if (value instanceof String) {
            size = ((String) ((String) value)).length();
        } else {
            throw new RuntimeException("Unsupported value type: " + value.getClass().getName());
        }

        execute(size, context);
    }
}
