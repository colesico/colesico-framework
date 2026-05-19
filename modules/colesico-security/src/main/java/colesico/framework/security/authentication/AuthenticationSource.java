package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Represents a transport-level participant in the authentication process.
 * <p>
 * An AuthenticationSource acts as a bridge between the security framework and specific
 * communication protocols such as HTTP, gRPC, or Message Queues. It is responsible
 * for extracting credentials and handling protocol-specific responses.
 */
public interface AuthenticationSource {

    /**
     * Extracts an {@link AuthenticationRequest} from the underlying transport.
     *
     * @return the request containing credentials, or {@code null} if no credentials
     * are present in this source.
     */
    AuthenticationRequest request();

    /**
     * Triggers a protocol-specific authentication challenge.
     * <p>
     * This is used for multi-step authentication (e.g., Digest, OAuth redirect,
     * or Multi-Factor Authentication) to prompt the client for further information.
     */
    default <C extends AuthenticationChallenge> void proceed(C challenge) {

    }

    /**
     * Notifies the source that the subject has been successfully authenticated.
     * <p>
     * This allows the source to perform post-authentication actions, such as
     * attaching the identity to a session or sending a success header.
     */
    default void authenticate(Identity<?> identity) {

    }

    /**
     * Notifies the source of a failed authentication attempt.
     * <p>
     * Allows the source to react to the failure, for example, by clearing
     * invalid credentials from the transport headers or logging the event.
     */
    default void unauthenticated(AuthenticationRequest request) {

    }

    /**
     * Notifies the source that the subject has been logged out.
     * <p>
     * This is used to clear protocol-specific security data, such as
     * invalidating a session cookie or clearing local security headers.
     */
    default void logout(Identity<?> identity) {

    }
}
