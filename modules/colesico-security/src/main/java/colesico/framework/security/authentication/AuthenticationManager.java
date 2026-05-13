package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Service for delegate authentication/logout to appropriate authenticator
 */
public interface AuthenticationManager {

    /**
     * Delegates authentication to acceptable authenticator
     */
    AuthenticationResult<?> authenticate(AuthenticationContext context);

    /**
     * Delegates the logout to acceptable authenticator that is selected by
     * claim {@link Identity#AUTHENTICATOR_CLAIM}
     */
    void logout(Identity<?> identity);
}
