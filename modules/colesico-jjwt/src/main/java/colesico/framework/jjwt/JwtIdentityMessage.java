package colesico.framework.jjwt;

import colesico.framework.security.Identity;

public record JwtIdentityMessage(Identity identity) implements JwtMessage{

}
