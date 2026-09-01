package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface LogoutMessage {
    Identity identity();

    record Default(Identity identity) implements LogoutMessage {

    }
}
