package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public sealed interface AuthenticationResult
        permits
        AuthenticationResult.Success,
        AuthenticationResult.Failure,
        AuthenticationResult.Stage {

    record Success(Identity identity) implements AuthenticationResult {
    }

    record Failure(Object message) implements AuthenticationResult {
    }

    record Stage(Object message) implements AuthenticationResult {
    }

    static Success success(Identity identity) {
        return new Success(identity);
    }

    static Failure failure(Object message) {
        return new Failure(message);
    }

    static Stage stage(Object message) {
        return new Stage(message);
    }
}
