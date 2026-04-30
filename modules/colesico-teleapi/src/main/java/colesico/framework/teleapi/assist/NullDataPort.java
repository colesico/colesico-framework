package colesico.framework.teleapi.assist;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@Singleton
public final class NullDataPort implements DataPort<TRContext<?, ?>, TWContext<?, ?>> {

    private static final Logger log = LoggerFactory.getLogger(NullDataPort.class);
    private final ThreadScope threadScope;

    public NullDataPort(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    public void provide() {
        threadScope.put(DataPort.SCOPE_KEY, this);
    }

    @Override
    public <V> V read(TRContext<?, ?> context) {
        log.debug("Read for context: {}", context);
        return null;
    }

    @Override
    public <V> V read(Type valueType) {
        log.debug("Read for value type: {}", valueType);
        return null;
    }

    @Override
    public <V> void write(V value, TWContext<?, ?> context) {
        log.debug("Write value: {}; context: {}", value, context);
    }

    @Override
    public <V> void write(V value, Type valueType) {
        log.debug("Write value: {}; value type: {}", value, valueType);
    }
}
