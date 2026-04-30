package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class SimpleDataPort implements DataPort<TRContext<?, ?>, TWContext<?, ?>> {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataPort.class);
    private final ThreadScope threadScope;
    private final Map<Type, Object> values = new ConcurrentHashMap<>();

    public SimpleDataPort(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    public void provide() {
        threadScope.put(DataPort.SCOPE_KEY, this);
    }

    public Map<Type, ?> values() {
        return values;
    }

    public void clear() {
        values.clear();
    }

    @Override
    public <V> V read(TRContext<?, ?> context) {
        log.debug("Read for context: {}", context);
        return (V) values.get(context.valueType());
    }

    @Override
    public <V, A> V read(Type valueType, A attributes) {
        log.debug("Read for value type: {}; attributes: {}", valueType, attributes);
        return (V) values.get(valueType);
    }

    @Override
    public <V> void write(V value, TWContext<?, ?> context) {
        log.debug("Write value: {}; context: {}", value, context);
        values.put(context.valueType(), value);
    }

    @Override
    public <V, A> void write(V value, Type valueType, A attributes) {
        log.debug("Write value: {}; value type: {}; attributes: {}", value, valueType, attributes);
        values.put(valueType, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SimpleDataPort {\n");
        values.forEach((k, v) -> {
            sb.append(k.getTypeName()).append("=").append(v).append("\n");
        });
        sb.append("}");
        return sb.toString();
    }
}
