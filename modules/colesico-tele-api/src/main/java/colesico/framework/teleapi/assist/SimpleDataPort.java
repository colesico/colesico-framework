package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.TaskScope;
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
public final class SimpleDataPort implements DataPort<ReadOptions, WriteOptions> {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataPort.class);
    private final TaskScope taskScope;

    // Key type changed to Type to support complex types (e.g., List<String>)
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

    // Returns Map<Type, ?> to match the updated field signature
    public Map<Type, ?> values() {
        return values;
    }

    public void clear() {
        values.clear();
    }

    /**
     * Reads a value using the given read options.
     *
     * @param baseType base type of the value to read
     * @param options  logical read options
     * @param <V>      value type
     * @return the deserialized value
     */
    @Override
    @SuppressWarnings("unchecked")
    public <V> V read(Type baseType, ReadOptions options) {
        log.debug("Read for valueType: {}; options: {}", baseType, options);
        return cast(values.get(baseType), baseType);
    }

    /**
     * Reads a value using default read options.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <V> V read(Type baseType) {
        log.debug("Read for valueType: {}", baseType);
        return cast(values.get(baseType), baseType);
    }

    /**
     * Reads a value using an attachment object.
     * The attachment is embedded to concrete read options (e.g., via {@link ReadOptions#attachment()})
     * and then delegated to {@link #read(Type, ReadOptions)}.
     *
     * @param attachment a message object for the reader
     */
    @Override
    @SuppressWarnings("unchecked")
    public <V> V read(Type baseType, Object attachment) {
        log.debug("Read for valueType: {}; attachment: {}", baseType, attachment);
        return cast(values.get(baseType), baseType);
    }

    /**
     * Writes a value using the given write options.
     *
     * @param value    value to write
     * @param baseType value base type
     * @param options  write options
     * @param <V>      value type
     */
    @Override
    public <V> void write(V value, Type baseType, WriteOptions options) {
        log.debug("Write value: {}; valueType: {}; options: {}", value, baseType, options);
        values.put(baseType, value);
    }

    /**
     * Writes a value using default write options.
     */
    @Override
    public <V> void write(V value, Type baseType) {
        log.debug("Write value: {}; valueType: {}", value, baseType);
        values.put(baseType, value);
    }

    /**
     * Writes a value using an attachment object.
     * The attachment is embedded to concrete write options and delegated to
     * {@link #write(Object, Type, WriteOptions)}.
     *
     * @param attachment a message object for the writer (not raw protocol)
     */
    @Override
    public <V> void write(V value, Type baseType, Object attachment) {
        log.debug("Write value: {}; valueType: {}; attachment: {}", value, baseType, attachment);
        values.put(baseType, value);
    }

    /**
     * Helper method for type-safe casting.
     * Uses Class.cast() if baseType is a regular Class instance.
     * Falls back to an unchecked cast for ParameterizedTypes due to Type Erasure.
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
}