package colesico.framework.security.authentication;


import colesico.framework.security.Identity;
import colesico.framework.security.SecurityException;

/**
 * Represents the outcome of an execution of {@link Authenticator}.
 */
public sealed interface AuthenticationOutcome permits AuthenticationOutcome.Failure, AuthenticationOutcome.Forward, AuthenticationOutcome.Skip, AuthenticationOutcome.Stage, AuthenticationOutcome.Success {

    AuthenticationResult result();

    /**
     * Definitively successful authentication.
     */
    record Success(Identity identity) implements AuthenticationOutcome {

        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.success(identity());
        }
    }

    /**
     * Definitively failed authentication.
     */
    record Failure(Object error) implements AuthenticationOutcome {
        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.failure(error());
        }
    }

    /**
     * Authentication required next stage/step and source/client interaction.
     */
    record Stage() implements AuthenticationOutcome {
        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.stage();
        }
    }

    /**
     * Authenticator abstained from decision.
     */
    record Skip(String reason) implements AuthenticationOutcome {
        @Override
        public AuthenticationResult result() {
            return AuthenticationResult.failure(reason);
        }
    }

    /**
     * Forward flow to specified authenticator.
     */
    record Forward<A extends AuthenticationMessage>(
            Authenticator<A, ?> authenticator,
            A message
    ) implements AuthenticationOutcome {
        @Override
        public AuthenticationResult result() {
            throw new SecurityException("Unsupported outcome");
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

    /**
     * Creates default forward outcome.
     */
    static <A extends AuthenticationMessage> Forward<A> forward(Authenticator<A, ?> authenticator,
                                                                A message) {
        return new Forward<>(authenticator, message);
    }


}