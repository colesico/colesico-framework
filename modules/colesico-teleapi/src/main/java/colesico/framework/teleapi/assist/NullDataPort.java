package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@Singleton
public final class NullDataPort implements DataPort<NullDataPort.ReadOptions, NullDataPort.WriteOptions> {

    private static final Logger log = LoggerFactory.getLogger(NullDataPort.class);
    private final TaskScope taskScope;

    public NullDataPort(TaskScope taskScope) {
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

    @Override
    public <V> V read(ReadOptions options) {
        log.debug("Read for options: {}", options);
        return null;
    }

    @Override
    public <V> V read(Type baseType, Object metadata) {
        return read(new ReadOptions(baseType, metadata));
    }

    @Override
    public <V> void write(V value, WriteOptions options) {
        log.debug("Write value: {}; options: {}", value, options);
    }

    @Override
    public <V> void write(V value, Type baseType, Object metadata) {
        write(value, new WriteOptions(baseType, metadata));
    }

    public record ReadOptions(
            Type baseType,
            Object metadata
    ) implements colesico.framework.teleapi.dataport.ReadOptions {
    }

    public record WriteOptions(
            Type baseType,
            Object metadata
    ) implements colesico.framework.teleapi.dataport.WriteOptions {
    }
}