package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Collection;
import java.util.Optional;

/**
 * Registry for managing and retrieving authentication components.
 * <p>
 * This registry allows locating the appropriate {@link Authenticator} or {@link AuthenticationSource}
 * based on an incoming request or an existing {@link Identity}.
 */
public interface AuthenticationRegistry {

    /**
     * Finds a suitable  {@link Authenticator}s  capable of processing the given {@link AuthenticationRequest}.
     *
     * @param request the authentication request to match an authenticator for.
     * @return an {@link Collection} containing the matched authenticators, or empty if none found.
     */
    Collection<Authenticator<?,?>> findAuthenticators(AuthenticationRequest request);

    /**
     * Retrieves the {@link Authenticator} that originally issued the given {@link Identity}.
     * <p>
     * The lookup is performed using the {@link Identity#AUTHENTICATOR_CLAIM} stored within the identity.
     *
     * @param identity the identity whose issuing authenticator is to be found.
     * @return an {@link Optional} containing the issuing authenticator, or empty if the claim
     * is missing or no matching authenticator is registered.
     */
    Optional<Authenticator<?,?>> findAuthenticator(Identity<?> identity);

    /**
     * Retrieves the {@link AuthenticationCallback} associated with the given {@link Identity}.
     * <p>
     * The lookup is performed using the {@link Identity#CALLBACK_CLAIM} stored within the identity.
     * This is typically used to route actions like logout back to the source that initiated
     * the authentication.
     *
     * @param identity the identity whose authentication source is to be found.
     * @return an {@link Optional} containing the authentication source, or empty if the claim
     * is missing or no matching source is registered.
     */
    Optional<AuthenticationSource<?, ?>> findCallback(Identity<?> identity);
}
