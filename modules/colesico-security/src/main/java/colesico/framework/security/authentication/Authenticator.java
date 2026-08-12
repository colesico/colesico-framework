package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * A strategy for authenticating/logout specific types of authentication,
 * i.e. database, ldap, etc
 *
 * <p>Implementations of this interface verify the credentials/token/etc provided in the
 * {@link AuthenticationRequest} and return an {@link AuthenticatorOutcome}
 * containing either the established {@code Identity} or failure details.
 * <p>
 * Register the {@link  Authenticator} instance with the IOC producer as
 * \@Produce(keyType = {@link  Authenticator}.class,
 * value = AuthenticatorInstace.class,
 * classed = AuthenticationContextInstance.class,
 * polyproduce=N)
 *
 * @param <RQ> the specific type of {@link AuthenticationRequest} this authenticator handles
 * @param <CB> the specific type of {@link AuthenticationCallback} this authenticator invokes
 */
public interface Authenticator<
        RQ extends AuthenticationRequest,
        CB extends AuthenticationCallback<?, ?, ?, ?>> {

    /**
     * Performs authentication using the provided request.
     *
     * @param callback optional callback
     */
    AuthenticatorOutcome authenticate(RQ request, CB callback);

    /**
     * Perform logout
     *
     * @param callback optional callback
     */
    default void logout(Identity<?> identity, CB callback) {
    }

}
