package colesico.framework.security.authentication;

/**
 * A strategy for authenticating specific types of authentication requests.
 *
 * <p>Implementations of this interface verify the credentials/token/etc provided in the
 * {@link AuthenticationContext} and return an {@link AuthenticationResult}
 * containing either the established {@code Identity} or failure details.
 *
 * @param <C> the specific type of {@link AuthenticationContext} this authenticator handles
 * <p>
 * Register the auth manager instance with the IOC producer as
 * \@Produce(keyType = AuthenticationProvider.class,
 * value = AuthProviderInstace.class,
 * classed = AuthenticationInstance.class)
 */
public interface Authenticator<C extends AuthenticationContext> {

    /**
     * Performs authentication using the provided context.
     */
    AuthenticationResult<?> authenticate(C context);
}
