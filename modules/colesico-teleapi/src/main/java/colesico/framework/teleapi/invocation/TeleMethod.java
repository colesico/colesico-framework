package colesico.framework.teleapi.invocation;

/**
 * Tele-method closure.
 * Represents proxy method of the {@link TeleFacade} to call target (service) method
 */
@FunctionalInterface
public interface TeleMethod {
    void invoke();
}
