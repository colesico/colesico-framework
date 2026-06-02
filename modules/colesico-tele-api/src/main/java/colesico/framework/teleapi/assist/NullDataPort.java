package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.ThreadScope;
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
    private final ThreadScope threadScope;

    public NullDataPort(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    public void provide() {
        threadScope.put(DataPort.SCOPE_KEY, this);
    }

    @Override
    public <V> V read(Class<V> valueType, ReadOptions options) {
        log.debug("Read for valueType: {}; options: {}", valueType, options);
        return null;
    }

    @Override
    public <V> V read(Class<V> valueType) {
        log.debug("Read for valueType: {}", valueType);
        return null;
    }

    @Override
    public <V> V read(Class<V> valueType, Object attachment) {
        log.debug("Read for valueType: {}; attachment: {}", valueType, attachment);
        return null;
    }

    @Override
    public <V> void write(V value, Class<V> valueType, WriteOptions options) {
        log.debug("Write value: {}; valueType: {}; options: {}", value, valueType, options);
    }

    @Override
    public <V> void write(V value, Class<V> valueType) {
        log.debug("Write value: {}; valueType: {}", value, valueType);
    }

    @Override
    public <V> void write(V value, Class<V> valueType, Object attachment) {
        log.debug("Write value: {}; valueType: {}; attachment: {}", value, valueType, attachment);
    }
}
