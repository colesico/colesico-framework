package colesico.framework.security.authentication;

/**
 * Defines the contract for an authentication flow.
 * Specifies the implementation for both authentication and logout processes.
 *
 * @param <A> the type of authentication message processed by this flow
 */
public interface Authentication<A extends AuthenticationMessage, L extends LogoutMessage> {

    /**
     * Executes the authentication process.
     * <p>
     * If the {@code message} parameter is {@code null}, the authentication process
     * is initiated from the very beginning. If a message is provided, the process
     * resumes from the specific entry point associated with that message.
     *
     * @param message optional authentication message;
     * @return the outcome of the authentication attempt
     */
    AuthenticationOutcome authenticate(A message);

    /**
     * Executes the logout process for the specified message.
     *
     * @param message mandatory message
     */
    void logout(L message);
}
