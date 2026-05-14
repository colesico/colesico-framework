package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Represents the party to be authenticated.
 * Its can be a HTTP Request/Response, gRPC, MQ Message, etc.
 */
public interface AuthenticationPeer {

    /**
     * Retrieve {@link AuthenticationRequest} from peer
     */
    AuthenticationRequest request();

    /**
     * Request to continue authentication (challenge)
     */
    <C> void proceed(C challenge);

    /**
     * Login notification
     */
    void login(Identity<?> identity);

    /**
     * Logout notification
     */
    void logout(Identity<?> identity);
}
