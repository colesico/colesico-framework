package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * A strategy for authenticating/logout specific types of authentication,
 * i.e. database, ldap, etc
 *
 * <p>Implementations of this interface verify the credentials/token/etc provided in the
 * {@link AuthenticationContext} and return an {@link AuthenticationResult}
 * containing either the established {@code Identity} or failure details.
 * <p>
 * Register the context manager instance with the IOC producer as
 * \@Produce(keyType = Authenticator.class,
 * value = AuthenticatorInstace.class,
 * classed = AuthenticationContextInstance.class)
 *
 * @param <C> the specific type of {@link AuthenticationContext} this authenticator handles
 */
public interface Authenticator<C extends AuthenticationContext> {

    /**
     * Performs authentication using the provided context.
     */
    AuthenticationResult<?> login(C context);

    /**
     * Perform logout
     */
    void logout(Identity<?> identity);
}
