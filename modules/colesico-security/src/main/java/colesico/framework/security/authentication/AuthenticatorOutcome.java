package colesico.framework.security.authentication;


import colesico.framework.security.Identity;

/**
 * Represents the outcome of an authentication attempt by {@link Authenticator}.
 */
public sealed interface AuthenticatorOutcome permits
        AuthenticatorOutcome.Success,
        AuthenticatorOutcome.Failure,
        AuthenticatorOutcome.Stage,
        AuthenticatorOutcome.Skip {

    AuthenticationResult result();

    /**
     * Definitively successful authentication.
     */
    record Success(Identity identity) implements AuthenticatorOutcome {

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
     * Authentication required next stage/step as source interaction.
     */
    record Stage() implements AuthenticatorOutcome {
        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.stage();
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
     * Creates default skip outcome.
     */
    static Skip skip(String reason) {
        return new Skip(reason);
    }

}