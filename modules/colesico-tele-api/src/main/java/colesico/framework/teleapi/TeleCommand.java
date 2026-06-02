package colesico.framework.teleapi;

/**
 * Tele-method closure.
 * Represents proxy method of the {@link TeleFacade} to call target (service) method
 */
@FunctionalInterface
public interface TeleCommand {

    String INVOKE_METHOD = "invoke";

    /**
     * Invoke target method
     */
    void execute();

}
