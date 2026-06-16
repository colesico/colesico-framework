package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class SimpleDataPort implements DataPort<ReadOptions, WriteOptions> {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataPort.class);
    private final TaskScope taskScope;
    private final Map<Class<?>, Object> values = new ConcurrentHashMap<>();

    public SimpleDataPort(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    public void provide() {
        taskScope.put(DataPort.SCOPE_KEY, this);
    }

    public void forTask(Runnable task) {
        taskScope.run(() -> {
            taskScope.put(DataPort.SCOPE_KEY, this);
            task.run();
        });
    }

    public Map<Class<?>, ?> values() {
        return values;
    }

    public void clear() {
        values.clear();
    }

    @Override
    public <V> V read(Class<V> valueType, ReadOptions options) {
        log.debug("Read for valueType: {}; options: {}", valueType, options);
        return valueType.cast(values.get(valueType));
    }

    @Override
    public <V> V read(Class<V> valueType) {
        log.debug("Read for valueType: {}", valueType);
        return valueType.cast(values.get(valueType));
    }

    @Override
    public <V> V read(Class<V> valueType, Object attachment) {
        log.debug("Read for valueType: {}; attachment: {}", valueType, attachment);
        return valueType.cast(values.get(valueType));
    }

    @Override
    public <V> void write(V value, Class<V> valueType, WriteOptions options) {
        log.debug("Write value: {}; valueType: {}; options: {}", value, valueType, options);
        values.put(valueType, value);
    }

    @Override
    public <V> void write(V value, Class<V> valueType) {
        log.debug("Write value: {}; valueType: {}", value, valueType);
        values.put(valueType, value);
    }

    @Override
    public <V> void write(V value, Class<V> valueType, Object attachment) {
        log.debug("Write value: {}; valueType: {}; attachment: {}", value, valueType, attachment);
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
