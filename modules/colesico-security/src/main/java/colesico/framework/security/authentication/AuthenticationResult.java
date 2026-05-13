package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface AuthenticationResult<E> {

    boolean isSuccess();

    Identity<?> identity();

    E error();

    record Success<E>(
            Identity<?> identity
    ) implements AuthenticationResult<E> {

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public E error() {
            return null;
        }
    }

    /**
     * Реализация провала
     */
    record Failure<E>(
            E error
    ) implements AuthenticationResult<E> {

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public Identity<?> identity() {
            return null;
        }

    }

    static <E> AuthenticationResult<E> success(Identity<?> identity, AuthenticationContext auth) {
        return new Success<>(identity);
    }

    static <E> AuthenticationResult<E> failure(E error) {
        return new Failure<>(error);
    }
}
