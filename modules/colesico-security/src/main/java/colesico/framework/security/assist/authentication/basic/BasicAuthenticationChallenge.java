package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.authentication.AuthenticationChallenge;

public record BasicAuthenticationChallenge(String realm) implements AuthenticationChallenge {

}
