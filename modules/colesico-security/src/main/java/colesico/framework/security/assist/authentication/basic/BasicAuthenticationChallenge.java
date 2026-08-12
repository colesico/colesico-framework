package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.authentication.AuthenticationOutcome;

public record BasicAuthenticationChallenge(String realm)
        implements AuthenticationOutcome.Stage {

}
