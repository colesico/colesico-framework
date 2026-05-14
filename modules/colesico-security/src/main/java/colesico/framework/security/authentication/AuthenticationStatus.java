package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Optional;

public sealed interface AuthenticationStatus
        permits AuthenticationStatus.Success,
        AuthenticationStatus.Failure,
        AuthenticationStatus.Continuation {

    /**
     * Successful authentication
     */
    record Success(Identity<?> identity) implements AuthenticationStatus {
    }

    /**
     * Authentication failed
     */
    record Failure<E>(E error) implements AuthenticationStatus {
    }

    /**
     * Authentication requires an additional step
     */
    record Continuation<C>(C challenge) implements AuthenticationStatus {
    }

    default boolean isSuccess() {
        return this instanceof AuthenticationStatus.Success;
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

    static AuthenticationStatus success(Identity<?> identity) {
        return new Success(identity);
    }

    static <E> AuthenticationStatus failure(E error) {
        return new Failure<>(error);
    }

    static <C> AuthenticationStatus continuation(C challenge) {
        return new Continuation<>(challenge);
    }
}
