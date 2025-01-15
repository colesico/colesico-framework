package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TRContext;
import colesico.framework.teleapi.TWContext;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class SimpleDataPort implements DataPort<TRContext, TWContext> {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataPort.class);
    private final ThreadScope threadScope;
    private final Map<Type, ? super Object> values = new ConcurrentHashMap<>();

    public SimpleDataPort(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    public void provide() {
        threadScope.put(DataPort.SCOPE_KEY, this);
    }

    public Map<Type,? super Object> getValues() {
        return values;
    }

    @Override
    public Object read(TRContext context) {
        log.debug("Read for context: {}", context);
        return values.get(context.getValueType());
    }

    @Override
    public Object read(Type valueType) {
        log.debug("Read for value type: {}", valueType);
        return values.get(valueType);
    }

    @Override
    public void write(Object value, TWContext context) {
        log.debug("Write value: {}; context: {}", value, context);
        values.put(context.getValueType(),value);
    }

    @Override
    public void write(Object value, Type valueType) {
        log.debug("Write value: {}; value type: {}", value, valueType);
        values.put(valueType,value);
    }
}
