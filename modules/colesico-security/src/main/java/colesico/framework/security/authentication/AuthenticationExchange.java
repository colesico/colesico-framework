package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Authentication exchange is represented by HTTP Request/Response, grpc, MQ Message, etc.
 */
public interface AuthenticationExchange {

    /**
     * Retrieve {@link AuthenticationContext}.
     */
    AuthenticationContext context();

    /**
     * Succeeded authentication callback
     */
    void login(Identity<?> identity);

    /**
     * Logout callback
     */
    void logout(Identity<?> identity);
}
