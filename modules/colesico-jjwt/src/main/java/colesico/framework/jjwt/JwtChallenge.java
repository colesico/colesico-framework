package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationChallenge;

public record JwtChallenge(Action action) implements AuthenticationChallenge {
    public enum Action {
        REFRESH_ACCESS_TOKEN
    }
}
