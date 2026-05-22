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
public final class NullDataPort implements DataPort<ReadOptions<?, ?>, WriteOptions<?, ?>> {

    private static final Logger log = LoggerFactory.getLogger(NullDataPort.class);
    private final ThreadScope threadScope;

    public NullDataPort(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    public void provide() {
        threadScope.put(DataPort.SCOPE_KEY, this);
    }

    @Override
    public <V> V read(ReadOptions<?, ?> query) {
        log.debug("Read for context: {}", query);
        return null;
    }

    @Override
    public <V, A> V read(Type valueType, A attachment) {
        log.debug("Read for value type: {}; attributes: {}", valueType, attachment);
        return null;
    }

    @Override
    public <V> void write(V value, WriteOptions<?, ?> options) {
        log.debug("Write value: {}; context: {}", value, options);
    }

    @Override
    public <V, P> void write(V value, Type valueType, P attachment) {
        log.debug("Write value: {}; value type: {}; payload: {}", value, valueType, attachment);
    }
}
