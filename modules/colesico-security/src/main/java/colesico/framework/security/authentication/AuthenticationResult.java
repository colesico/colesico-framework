package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Optional;

public sealed interface AuthenticationResult
        permits AuthenticationResult.Success,
        AuthenticationResult.Failure,
        AuthenticationResult.Continuation {

    /**
     * Successful authentication
     */
    record Success(Identity<?> identity) implements AuthenticationResult {
    }

    /**
     * Authentication failed
     */
    record Failure<E>(E error) implements AuthenticationResult {
    }

    /**
     * Authentication requires an additional step
     */
    record Continuation<C extends AuthenticationChallenge>(C challenge) implements AuthenticationResult {
    }

    default boolean isSuccess() {
        return this instanceof AuthenticationResult.Success;
    }

    default boolean isFailure() {
        return this instanceof Failure<?>;
    }

    default boolean isContinuation() {
        return this instanceof Continuation<?>;
    }

    default Optional<Identity<?>> getIdentity() {
        return this instanceof Success(Identity<?> identity) ? Optional.of(identity) : Optional.empty();
    }

    default <E> Optional<E> getError() {
        return this instanceof Failure(Object error) ? (Optional<E>) Optional.of(error) : Optional.empty();
    }

    default <C> Optional<C> getChallenge() {
        return this instanceof Continuation(Object challenge) ? Optional.of((C) challenge) : Optional.empty();
    }

    static AuthenticationResult success(Identity<?> identity) {
        return new Success(identity);
    }

    static <E> AuthenticationResult failure(E error) {
        return new Failure<>(error);
    }

    static <C extends AuthenticationChallenge> AuthenticationResult continuation(C challenge) {
        return new Continuation<>(challenge);
    }
}
