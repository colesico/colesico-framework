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
        taskScope.forTask(() -> {
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
    public <V> V read(Class<V> baseType, ReadOptions options) {
        log.debug("Read for valueType: {}; options: {}", baseType, options);
        return baseType.cast(values.get(baseType));
    }

    @Override
    public <V> V read(Class<V> baseType) {
        log.debug("Read for valueType: {}", baseType);
        return baseType.cast(values.get(baseType));
    }

    @Override
    public <V> V read(Class<V> baseType, Object attachment) {
        log.debug("Read for valueType: {}; attachment: {}", baseType, attachment);
        return baseType.cast(values.get(baseType));
    }

    @Override
    public <V> void write(V value, Class<V> baseType, WriteOptions options) {
        log.debug("Write value: {}; valueType: {}; options: {}", value, baseType, options);
        values.put(baseType, value);
    }

    @Override
    public <V> void write(V value, Class<V> baseType) {
        log.debug("Write value: {}; valueType: {}", value, baseType);
        values.put(baseType, value);
    }

    @Override
    public <V> void write(V value, Class<V> baseType, Object attachment) {
        log.debug("Write value: {}; valueType: {}; attachment: {}", value, baseType, attachment);
        values.put(baseType, value);
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
