package colesico.framework.jjwt;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class JwtAuthenticator implements Authenticator<JwtRequest, AuthenticationChallenge> {

    private final JwtTokenUtils tokenUtils;

    @Inject
    public JwtAuthenticator(JwtTokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public AuthenticationResult<AuthenticationChallenge> authenticate(JwtRequest request) {
        try {
            Claims claims = tokenUtils.parseAccessToken(request.accessToken());

            var tokenType = claims.get(JwtTokenUtils.TOKEN_TYPE_CLAIM);
            if (!JwtTokenUtils.ACCESS_TOKEN_TYPE.equals(tokenType)) {
                return AuthenticationResult.failure("Invalid accessToken type: " + tokenType);
            }

            Map<String, Object> identityClaims = new HashMap<>(claims);
            identityClaims.put(Identity.AUTHENTICATOR_CLAIM, JwtAuthenticator.class);
            Identity<?> identity = Identity.Default.of(claims.getSubject(), identityClaims);
            return AuthenticationResult.success(identity);
        } catch (ExpiredJwtException e) {
            return AuthenticationResult.challenge(JwtChallenge(JwtChallenge.Action.REFRESH_ACCESS_TOKEN));
        } catch (Exception e) {
            return AuthenticationResult.failure("InvalidToken");
        }
    }

    @Override
    public void logout(Identity<?> identity) {
        // nop
    }
}
