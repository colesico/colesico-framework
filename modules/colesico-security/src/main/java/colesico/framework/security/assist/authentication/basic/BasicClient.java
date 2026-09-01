package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationMessage;
import colesico.framework.security.authentication.Authenticator;

/**
 * Provides contract to interacting with client side in authentication process.
 * <p>
 * This component acts as an intermediary that collects credentials from the
 * actual subject (via HTTP or other communication channels), transforms them into
 * a message, and handles authentication callbacks from the {@link Authenticator#authenticate(AuthenticationMessage)}.
 */
public interface BasicClient {

    /**
     * Extracts the basic authentication message from the incoming source
     * (such as an HTTP/gRPC request, RabbitMQ message, etc.).
     *
     * @return the extracted basic message containing credentials or routing data
     */
    BasicMessage message();

    /**
     * Triggers a challenge response process for the specified security realm,
     * typically prompting the client to provide credentials.
     *
     * @param realm the security realm for which the challenge is issued
     */
    void challenge(String realm);

    /**
     * Performs a logout operation, invalidating the session associated
     * with the specified identity.
     *
     * @param identity the identity of the subject to be logged out
     */
    void logout(Identity identity);

}