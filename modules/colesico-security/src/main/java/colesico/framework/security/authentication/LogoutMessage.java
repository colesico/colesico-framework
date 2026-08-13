package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface LogoutMessage extends AuthenticationMessage {
    Identity<?> identity();

    record Default(Identity<?> identity) implements LogoutMessage {

    }
}
