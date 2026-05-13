package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Authentication Source is represented by HTTP Request/Response, MQ Message, etc.
 */
public interface AuthenticationSource {

    /**
     * Retrieve {@link AuthenticationContext} from the source.
     */
    AuthenticationContext context();

    /**
     * The succeeded authentication callback
     */
    void onAuthenticated(Identity<?> identity);

    /**
     * Logout callback
     */
    void onLogout(Identity<?> identity);
}
