package colesico.framework.security.authentication;

/**
 * A strategy for authenticating specific types of authentication requests.
 *
 * <p>Implementations of this interface verify the credentials/token/etc provided in the
 * {@link AuthenticationRequest} and return an {@link AuthenticationResult}
 * containing either the established {@code Identity} or failure details.
 *
 * @param <A> the specific type of {@link AuthenticationRequest} this authenticator handles
 * <p>
 * Register the auth manager instance with the IOC producer as
 * \@Produce(keyType = AuthenticationProvider.class,
 * value = AuthProviderInstace.class,
 * classed = AuthenticationInstance.class)
 */
public interface Authenticator<A extends AuthenticationRequest> {

    /**
     * Authenticate user
     */
    AuthenticationResult<?> authenticate(A authRequest);
}
