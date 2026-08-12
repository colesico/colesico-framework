package colesico.framework.security.authentication;


import colesico.framework.security.Identity;

/**
 * Represents the outcome of an authentication attempt by {@link Authenticator}.
 */
public sealed interface AuthenticationOutcome
        permits
        AuthenticationOutcome.Success,
        AuthenticationOutcome.Failure,
        AuthenticationOutcome.Stage,
        AuthenticationOutcome.Skip {

    AuthenticationResult toResult();

    /**
     * Definitively successful authentication.
     */
    non-sealed interface Success extends AuthenticationOutcome {
        Identity identity();

        @Override
        default AuthenticationResult toResult() {
            return AuthenticationResult.success(identity());
        }
    }

    /**
     * Definitively failed authentication.
     */
    non-sealed interface Failure extends AuthenticationOutcome {
        Object error();

        @Override
        default AuthenticationResult toResult() {
            return AuthenticationResult.failure(error());
        }
    }

    /**
     * Authentication required next stage/step as source interaction.
     */
    non-sealed interface Stage extends AuthenticationOutcome {
        @Override
        default AuthenticationResult toResult() {
            return AuthenticationResult.stage();
        }
    }

    /**
     * Authenticator abstained from decision.
     */
    record Skip(String reason) implements AuthenticationOutcome {
        @Override
        public AuthenticationResult toResult() {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    /**
     * Creates default successful outcome
     */
    static Success success(final Identity identity) {
        return () -> identity;
    }

    /**
     * Creates default failure outcome.
     */
    static Failure failure(final Object error) {
        return () -> error;
    }

    /**
     * Creates default skip outcome.
     */
    static Skip skip(String reason) {
        return new Skip(reason);
    }

}