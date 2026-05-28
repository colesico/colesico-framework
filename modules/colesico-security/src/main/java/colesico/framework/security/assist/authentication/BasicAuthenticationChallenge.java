package colesico.framework.security.assist.authentication;

import colesico.framework.security.authentication.AuthenticationChallenge;

public record BasicAuthenticationChallenge(String realm) implements AuthenticationChallenge {

}
