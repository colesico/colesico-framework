package colesico.framework.teleapi;

import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;

/**
 * Tele-method closure.
 * Represents proxy method of the {@link TeleFacade} to call target (service) method
 */
@FunctionalInterface
public interface TeleMethod<R extends TRContext, W extends TWContext> {

    String DATA_PORT_PARAM = "dataPort";

    void invoke(DataPort<R, W> dataPort);

    @SuppressWarnings("unchecked")
    default void execute(DataPort<? extends TRContext, ? extends TWContext> dataPort) {
        this.invoke((DataPort<R, W>) dataPort);
    }
}
