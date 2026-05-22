package colesico.framework.teleapi;

import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;

/**
 * Tele-method closure.
 * Represents proxy method of the {@link TeleFacade} to call target (service) method
 */
@FunctionalInterface
public interface TeleCommand<R extends ReadOptions<?>, W extends WriteOptions<?>> {

    String EXECUTE_METHOD = "execute";
    String INVOKE_METHOD = "invoke";
    String DATA_PORT_PARAM = "dataPort";

    /**
     * Invoke target method
     */
    void invoke(DataPort<R, W> dataPort);

    /**
     * To pass data-port without casting
     */
    @SuppressWarnings("unchecked")
    default void execute(DataPort<? extends ReadOptions<?>, ? extends WriteOptions<?>> dataPort) {
        this.invoke((DataPort<R, W>) dataPort);
    }
}
