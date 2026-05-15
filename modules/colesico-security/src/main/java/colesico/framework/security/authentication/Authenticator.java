package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * A strategy for authenticating/logout specific types of authentication,
 * i.e. database, ldap, etc
 *
 * <p>Implementations of this interface verify the credentials/token/etc provided in the
 * {@link AuthenticationRequest} and return an {@link AuthenticationResult}
 * containing either the established {@code Identity} or failure details.
 * <p>
 * Register the {@link  Authenticator} instance with the IOC producer as
 * \@Produce(keyType = {@link  Authenticator}.class,
 * value = AuthenticatorInstace.class,
 * classed = AuthenticationContextInstance.class,
 * polyproduce=N)
 *
 * @param <R> the specific type of {@link AuthenticationRequest} this authenticator handles
 */
public interface Authenticator<R extends AuthenticationRequest> {

    /**
     * Check authenticator supports given request
     */
    boolean supports(R request);

    /**
     * Performs authentication using the provided context.
     */
    AuthenticationResult login(R request);

}
