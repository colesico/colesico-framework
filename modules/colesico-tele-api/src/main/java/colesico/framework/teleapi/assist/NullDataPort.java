package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@Singleton
public final class NullDataPort implements DataPort<ReadOptions, WriteOptions> {

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
    public <V> V read(Type baseType, ReadOptions options) {
        log.debug("Read for valueType: {}; options: {}", baseType, options);
        return null;
    }

    @Override
    public <V> V read(Type baseType) {
        log.debug("Read for valueType: {}", baseType);
        return null;
    }

    @Override
    public <V> V read(Type baseType, Object attachment) {
        log.debug("Read for valueType: {}; attachment: {}", baseType, attachment);
        return null;
    }

    @Override
    public <V> void write(V value, Type baseType, WriteOptions options) {
        log.debug("Write value: {}; valueType: {}; options: {}", value, baseType, options);
    }

    @Override
    public <V> void write(V value, Type baseType) {
        log.debug("Write value: {}; valueType: {}", value, baseType);
    }

    @Override
    public <V> void write(V value, Type baseType, Object attachment) {
        log.debug("Write value: {}; valueType: {}; attachment: {}", value, baseType, attachment);
    }
}
