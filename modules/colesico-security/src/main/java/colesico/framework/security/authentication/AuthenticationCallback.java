package colesico.framework.security.authentication;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;

/**
 * Callback for handling authentication outcomes for a specific request or source.
 * Extends this interface to add more specific callbacks.
 */
public interface AuthenticationCallback<LI, LO> {

    /**
     * Callback ID class.
     * This id used to obtain this callback from  {@link Ioc} with {@link ClassedKey}
     */
    Class<?> id();

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
