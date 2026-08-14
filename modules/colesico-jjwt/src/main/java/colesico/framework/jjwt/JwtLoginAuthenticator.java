package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationMessage;

import java.util.Map;

public class JwtLoginAuthenticator implements Authenticator  {

   public record Request(Map<String, Object> claims) implements AuthenticationMessage {

   }
}
