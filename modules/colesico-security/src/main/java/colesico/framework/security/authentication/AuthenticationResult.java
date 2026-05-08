package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Optional;

public interface AuthenticationResult<E> {

    record Success<E>(Identity<?> identity) implements AuthenticationResult<E> {
    }

    record Failure<E>(E error) implements AuthenticationResult<E> {
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default Optional<Identity<?>> getIdentity() {
        return this instanceof Success<E>(Identity<?> identity) ? Optional.of(identity) : Optional.empty();
    }

    default Optional<E> getError() {
        return this instanceof Failure<E>(E error) ? Optional.of(error) : Optional.empty();
    }

    static <E> Success<E> success(Identity<?> identity) {
        return new Success<>(identity);
    }

    static <E> Failure<E> failure(E error) {
        return new Failure<>(error);
    }
}
