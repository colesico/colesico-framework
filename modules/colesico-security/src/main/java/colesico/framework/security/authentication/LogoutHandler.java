package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Logout handler
 * Register the {@link  LogoutHandler} instance with the IOC producer as
 * \@Produce(keyType = {@link  LogoutHandler}.class,
 * value = LogoutHandlerInstace.class,
 * named = "logout-handler",
 * polyproduce=N)
 *
 * where logout-handler is the name that {@link Authenticator} should put to claim
 * {@link Identity#LOGOUT_HANDLER_CLAIM}
 */
public interface LogoutHandler {

    /**
     * Perform logout
     */
    void logout(Identity<?> identity);
}
