package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Represents the final or intermediate result of an authentication process.
 */
public sealed interface AuthenticationResult
        permits AuthenticationResult.Abstained, AuthenticationResult.Continuation, AuthenticationResult.Failure, AuthenticationResult.Success {

    /**
     * Successful authentication
     */
    record Success(Identity<?> identity) implements AuthenticationResult {
    }

    /**
     * Authentication failed definitively
     */
    record Failure(String error) implements AuthenticationResult {
    }

    /**
     * Authentication requires an additional step/challenge (e.g. MFA)
     * Теперь тип C жестко связан с типом результата на уровне компиляции.
     */
    record Continuation<C extends AuthenticationChallenge>(C challenge) implements AuthenticationResult {
    }

    /**
     * Authenticator abstained from decision.
     * Move to the next authenticator in the chain.
     */
    record Abstained(String reason) implements AuthenticationResult {
    }

    // --- Статические фабричные методы с корректным выводом типов ---

    static AuthenticationResult success(Identity<?> identity) {
        return new Success(identity);
    }

    static AuthenticationResult failure(String error) {
        return new Failure(error);
    }

    static <C extends AuthenticationChallenge> AuthenticationResult challenge(C challenge) {
        return new Continuation<>(challenge);
    }

    static AuthenticationResult abstained(String reason) {
        return new Abstained(reason);
    }
}