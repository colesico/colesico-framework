package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Represents the party to be authenticated.
 * It can be a HTTP Request/Response, gRPC, MQ Message, etc.
 */
public interface AuthenticationPeer {

    /**
     * Retrieve {@link AuthenticationRequest} from peer
     */
    AuthenticationRequest request();

    /**
     * Request to continue authentication (challenge)
     */
    <C extends AuthenticationChallenge> void proceed(C challenge);

    /**
     * Identity is authenticated notification
     */
    void authenticate(Identity<?> identity);

    /**
     * Identity is logged out notification
     */
    void logout(Identity<?> identity);
}
