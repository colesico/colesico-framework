package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Callback for handling authentication outcomes for a specific request or source.
 */
public interface AuthenticationCallback<SC, FL, ST, LO> {

    /**
     * Handle the subject has been successfully authenticated.
     * <p>
     * This allows to perform post-authentication actions, such as
     * attaching the identity to a session or sending a success header.
     */
    default void onSuccess(SC success) {
    }

    /**
     * Handle failed authentication attempt.
     * <p>
     * Allows to react to the failure, for example, by clearing
     * invalid credentials from the transport headers or logging the event.
     */
    default void onFailure(FL failure) {
    }

    /**
     * Handle protocol-specific authentication next step/challenge.
     * <p>
     * This is used for multi-step authentication (e.g., Digest, OAuth redirect,
     * or Multi-Factor Authentication) to prompt the client for further information.
     */
    default void onStage(ST stage) {
    }

    /**
     * Handle subject has been logged out.
     * <p>
     * This is used to clear protocol-specific security data, such as
     * invalidating a session cookie or clearing local security headers.
     */
    default void onLogout(LO logout) {
    }

}
