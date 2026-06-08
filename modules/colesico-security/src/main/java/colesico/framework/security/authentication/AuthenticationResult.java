package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

/**
 * Represents the outcome of an authentication attempt.
 *
 * @param <C> the type of authentication challenge required for continuation
 */
public sealed interface AuthenticationResult<C extends AuthenticationChallenge>
        permits AuthenticationResult.Success, AuthenticationResult.Failure, AuthenticationResult.Continuation, AuthenticationResult.Skip {

    /**
     * Successful authentication.
     */
    record Success<C extends AuthenticationChallenge>(Identity<?> identity) implements AuthenticationResult<C> {
    }

    /**
     * Definitively failed authentication.
     */
    record Failure<C extends AuthenticationChallenge, E>(E error) implements AuthenticationResult<C> {
    }

    /**
     * Authenticator abstained from decision.
     */
    record Skip<C extends AuthenticationChallenge>(String reason) implements AuthenticationResult<C> {
    }

    /**
     * Authentication requires an additional challenge.
     */
    record Continuation<C extends AuthenticationChallenge>(C challenge) implements AuthenticationResult<C> {
    }

    /**
     * Creates a successful authentication result adapted to the required challenge type.
     */
    static <T extends AuthenticationChallenge> AuthenticationResult<T> success(Identity<?> identity) {
        return new Success<>(identity);
    }

    /**
     * Creates a failure authentication result adapted to the required challenge type.
     */
    static <T extends AuthenticationChallenge, E> AuthenticationResult<T> failure(E error) {
        return new Failure<>(error);
    }

    /**
     * Creates an abstained authentication result adapted to the required challenge type.
     */
    static <T extends AuthenticationChallenge> AuthenticationResult<T> skip(String reason) {
        return new Skip<>(reason);
    }

    /**
     * Creates a continuation authentication result with the specific challenge.
     */
    static <T extends AuthenticationChallenge> AuthenticationResult<T> challenge(T challenge) {
        return new Continuation<>(challenge);
    }
}