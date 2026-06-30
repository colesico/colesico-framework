package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TeleReader;
import colesico.framework.teleapi.dataport.TeleWriter;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class SimpleDataPort implements DataPort<SimpleDataPort.ReadOptions, SimpleDataPort.WriteOptions> {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataPort.class);
    private final TaskScope taskScope;

    private final Map<Type, Object> values = new ConcurrentHashMap<>();

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

    public Map<Type, ?> values() {
        return values;
    }

    public void clear() {
        values.clear();
    }

    /**
     * Reads a value using the given read options.
     */
    @Override
    public <V> V read(ReadOptions options) {
        log.debug("Read for options: {}", options);
        return cast(values.get(options.baseType()), options.baseType());
    }

    /**
     * Reads the value by baseType and metadata.
     * The internal implementation creates appropriate {@link colesico.framework.teleapi.dataport.ReadOptions}
     * and delegates the call to {@link #read(ReadOptions)}.
     */
    @Override
    public <V> V read(Type baseType, Object metadata) {
        return read(new ReadOptions(baseType, metadata));
    }

    /**
     * Writes a value using the given write options.
     */
    @Override
    public <V> void write(V value, WriteOptions options) {
        log.debug("Write value with options: {}", options);
        values.put(options.baseType(), value);
    }

    /**
     * Writes the value by baseType and metadata.
     * The internal implementation creates appropriate {@link colesico.framework.teleapi.dataport.WriteOptions}
     * and delegates the call to {@link #write(Object, WriteOptions)}.
     */
    @Override
    public <V> void write(V value, Type baseType, Object metadata) {
        write(value, new WriteOptions(baseType, metadata));
    }

    /**
     * Helper method for type-safe casting.
     */
    @SuppressWarnings("unchecked")
    private <V> V cast(Object obj, Type baseType) {
        if (obj == null) {
            return null;
        }
        if (baseType instanceof Class<?>) {
            return ((Class<V>) baseType).cast(obj);
        }
        return (V) obj;
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

    public record ReadOptions(Type baseType,
                              Object metadata
    ) implements colesico.framework.teleapi.dataport.ReadOptions<TeleReader<?, ?>> {
    }

    public record WriteOptions(Type baseType,
                               Object metadata
    ) implements colesico.framework.teleapi.dataport.WriteOptions<TeleWriter<?, ?>> {
    }
}