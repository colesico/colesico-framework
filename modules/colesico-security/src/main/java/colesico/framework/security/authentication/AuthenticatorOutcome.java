package colesico.framework.security.authentication;


import colesico.framework.security.Identity;

/**
 * Represents the outcome of an authentication attempt by {@link Authenticator}.
 */
public sealed interface AuthenticatorOutcome permits
        AuthenticatorOutcome.Success,
        AuthenticatorOutcome.Failure,
        AuthenticatorOutcome.Next,
        AuthenticatorOutcome.Stage,
        AuthenticatorOutcome.Skip {

    AuthenticationResult result();

    /**
     * Definitively successful authentication.
     */
    record Success(Identity<?> identity) implements AuthenticatorOutcome {

        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.success(identity());
        }
    }

    /**
     * Definitively failed authentication.
     */
    record Failure(Object error) implements AuthenticatorOutcome {
        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.failure(error());
        }
    }

    /**
     * Authentication required next stage/step and source/client interaction.
     */
    record Stage() implements AuthenticatorOutcome {
        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.stage();
        }
    }

    /**
     * Transitional outcome that injects a specific next authenticator into the execution
     * chain with an updated request.
     *
     * @param request       the modified request payload for the next step
     * @param authenticator the authenticator to execute next
     */
    record Next(AuthenticatorRequest request, Authenticator<?, ?> authenticator) implements AuthenticatorOutcome {
        @Override
        public AuthenticationResult result() {
            throw new SecurityException("Next outcome is transitional and cannot be processed as a final result");
        }
    }

    /**
     * Authenticator abstained from decision.
     */
    record Skip(String reason) implements AuthenticatorOutcome {
        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.failure(reason);
        }
    }


    /**
     * Creates default successful outcome
     */
    static Success success(final Identity identity) {
        return new Success(identity);
    }

    /**
     * Creates default failure outcome.
     */
    static Failure failure(final Object error) {
        return new Failure(error);
    }

    static Stage stage() {
        return new Stage();
    }

    /**
     * Creates a transitional outcome to proceed to the specified next authenticator.
     */
    static Next next(final AuthenticatorRequest request, final Authenticator<?,?> authenticator) {
        return new Next(request, authenticator);
    }

    /**
     * Creates default skip outcome.
     */
    static Skip skip(String reason) {
        return new Skip(reason);
    }

}