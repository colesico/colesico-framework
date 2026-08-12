package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public sealed interface AuthenticationResult
        permits
        AuthenticationResult.Success,
        AuthenticationResult.Failure,
        AuthenticationResult.Stage {

    record Success(Identity identity) implements AuthenticationResult {

    }

    record Failure(Object error) implements AuthenticationResult {

    }

    record Stage() implements AuthenticationResult {

    }

    static Success success(Identity identity) {
        return new Success(identity);
    }

    static Failure failure(Object error) {
        return new Failure(error);
    }

    static Stage stage() {
        return new Stage();
    }
}
