package colesico.framework.security.authentication;

import colesico.framework.security.SecurityException;

public class UnauthenticatedException extends SecurityException {

    public UnauthenticatedException() {
    }

    public UnauthenticatedException(String s) {
        super(s);
    }

    public UnauthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthenticatedException(Throwable cause) {
        super(cause);
    }
}
