package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class SimpleDataPort implements DataPort<ReadOptions<?, ?>, WriteOptions<?, ?>> {

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
    public <V> V read(ReadOptions<?, ?> query) {
        log.debug("Read for context: {}", query);
        return (V) values.get(query.valueType());
    }

    @Override
    public <V, A> V read(Type valueType, A attachment) {
        log.debug("Read for value type: {}; attributes: {}", valueType, attachment);
        return (V) values.get(valueType);
    }

    @Override
    public <V> void write(V value, WriteOptions<?, ?> options) {
        log.debug("Write value: {}; context: {}", value, options);
        values.put(options.valueType(), value);
    }

    @Override
    public <V, P> void write(V value, Type valueType, P attachment) {
        log.debug("Write value: {}; value type: {}; payload: {}", value, valueType, attachment);
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
