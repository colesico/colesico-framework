package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory, thread-safe implementation of {@link DataPort} designed primarily for
 * testing, prototyping, or local execution environments.
 *
 * <p>This implementation stores all transmitted and received data internally within a
 * {@link ConcurrentHashMap} keyed by {@link Type}. It bypasses real-world transport mechanisms
 * or databases, providing a lightweight "fake" alternative that operates entirely in memory.
 *
 * <p>Integration with {@link TaskScope} allows this port to be bound to specific asynchronous
 * tasks or execution contexts via {@link #forTask(Runnable)}.
 *
 * @see DataPort
 * @see TaskScope
 */
@Singleton
public class SimpleDataPort implements DataPort<SimpleDataPort.ReadOptions, SimpleDataPort.WriteOptions> {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataPort.class);

    private final TaskScope taskScope;

    private final Map<String, Object> values = new ConcurrentHashMap<>();

    public SimpleDataPort(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    public void forTask(Runnable task) {
        taskScope.forTask(() -> {
            taskScope.put(DataPort.SCOPE_KEY, this);
            task.run();
        });
    }

    public Map<String, Object> values() {
        return new HashMap<>(values);
    }

    public void clear() {
        values.clear();
    }

    public String key(Type baseType, String name) {
        StringBuilder key = new StringBuilder(baseType.getTypeName());
        if (name != null) {
            key.append(":").append(name);
        }
        return key.toString();
    }

    /**
     * Reads a value using the given read options.
     */
    @Override
    public <V> V read(ReadOptions options) {
        log.debug("Read for options: {}", options);
        return cast(values.get(key(options.baseType(), options.name())), options.baseType());
    }

    /**
     * Reads the value by baseType and metadata.
     * The internal implementation creates appropriate {@link colesico.framework.teleapi.dataport.ReadOptions}
     * and delegates the call to {@link #read(ReadOptions)}.
     */
    @Override
    public <V> V read(Type baseType, Object metadata) {
        return read(ReadOptions.builder(baseType).metadata(metadata).build());
    }

    /**
     * Writes a value using the given write options.
     */
    @Override
    public <V> void write(V value, WriteOptions options) {
        log.debug("Write value with options: {}", options);
        values.put(key(options.baseType(), options.name()), value);
    }

    /**
     * Writes the value by baseType and metadata.
     * The internal implementation creates appropriate {@link colesico.framework.teleapi.dataport.WriteOptions}
     * and delegates the call to {@link #write(Object, WriteOptions)}.
     */
    @Override
    public <V> void write(V value, Type baseType, Object metadata) {
        write(value, WriteOptions.builder(baseType).metadata(metadata).build());
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
            sb.append(k).append("=").append(v).append("\n");
        });
        sb.append("}");
        return sb.toString();
    }

    public record ReadOptions(Type baseType,
                              String name,
                              Object metadata
    ) implements colesico.framework.teleapi.dataport.ReadOptions {

        public static Builder builder(Type baseType) {
            return new Builder(baseType);
        }

        public static class Builder {
            private final Type baseType;
            private String name;
            private Object metadata;

            public Builder(Type baseType) {
                this.baseType = baseType;
            }

            public ReadOptions build() {
                return new ReadOptions(baseType, name, metadata);
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder metadata(Object metadata) {
                this.metadata = metadata;
                return this;
            }

        }

    }

    public record WriteOptions(Type baseType,
                               String name,
                               Object metadata
    ) implements colesico.framework.teleapi.dataport.WriteOptions {

        public static Builder builder(Type baseType) {
            return new Builder(baseType);
        }

        public static class Builder {
            private final Type baseType;
            private String name;
            private Object metadata;

            public Builder(Type baseType) {
                this.baseType = baseType;
            }

            public WriteOptions build() {
                return new WriteOptions(baseType, name, metadata);
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder metadata(Object metadata) {
                this.metadata = metadata;
                return this;
            }

        }
    }
}