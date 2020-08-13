package colesico.framework.teleapi;

/**
 * Is used to retrieve target method parameters values from tele data port, invoke target method and puts back a result.
 *
 * @param <T> Target (service)  whose method will be invoked
 * @param <P> Data port
 */
@FunctionalInterface
public interface MethodInvoker<T, P extends DataPort> {
    String TARGET_PARAM = "target";
    String DATA_PORT_PARAM = "dataPort";

    void invoke(T target, P dataPort);
}
