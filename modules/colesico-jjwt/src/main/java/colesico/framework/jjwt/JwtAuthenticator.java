package colesico.framework.jjwt;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationOutcome;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class JwtAuthenticator implements Authenticator<JwtRequest, JwtCallback> {

    private final JwtTokenUtils tokenUtils;

    @Inject
    public JwtAuthenticator(JwtTokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public AuthenticationOutcome authenticate(JwtRequest request, JwtCallback callback) {
        try {
            Claims claims = tokenUtils.parseAccessToken(request.accessToken());

            var tokenType = claims.get(JwtTokenUtils.TOKEN_TYPE_CLAIM);
            if (!JwtTokenUtils.ACCESS_TOKEN_TYPE.equals(tokenType)) {
                return AuthenticationOutcome.failure("Invalid accessToken type: " + tokenType);
            }

            Map<String, Object> identityClaims = new HashMap<>(claims);
            identityClaims.put(Identity.AUTHENTICATION_CLAIM, JwtAuthenticator.class);
            Identity<?> identity = Identity.Default.of(claims.getSubject(), identityClaims);
            return AuthenticationOutcome.success(identity);
        } catch (ExpiredJwtException e) {
            //  return AuthenticationResult.challenge(JwtChallenge(JwtChallenge.Action.REFRESH_ACCESS_TOKEN));
            return null;
        } catch (Exception e) {
            return AuthenticationOutcome.failure("InvalidToken");
        }
    }

    @Override
    public void logout(Identity<?> identity) {
        // nop
    }
}
