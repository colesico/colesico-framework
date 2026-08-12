package colesico.framework.security.authentication;

/**
 * Callback for handling authentication outcomes for a specific request or source.
 * Extends this interface to add more specific callbacks.
 */
public interface AuthenticationCallback<LI, LO> {

    /**
     * Default handle the subject has been successfully authenticated.
     * <p>
     * This allows to perform post-authentication actions, such as
     * attaching the identity to a session or sending a success header.
     */
    default void onLogin(LI payload) {
    }

    /**
     * Handle subject has been logged out.
     * <p>
     * This is used to clear protocol-specific security data, such as
     * invalidating a session cookie or clearing local security headers.
     */
    default void onLogout(LO payload) {
    }

}
