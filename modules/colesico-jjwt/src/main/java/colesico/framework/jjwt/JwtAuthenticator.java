package colesico.framework.jjwt;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationOutcome;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.LogoutMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class JwtAuthenticator implements Authenticator<JwtMessage, LogoutMessage> {

    protected final JwtTokenUtils tokenUtils;
    protected final JwtClient client;

    @Inject
    public JwtAuthenticator(JwtTokenUtils tokenUtils, JwtClient client) {
        this.tokenUtils = tokenUtils;
        this.client = client;
    }

    @Override
    public AuthenticationOutcome authenticate(JwtMessage message) {
        if (message instanceof JwtIdentityMessage jim) {
            return releaseTokens(jim.identity().id(), jim.identity().claims());
        }
        return checkTokens();
    }

    protected AuthenticationOutcome checkTokens() {

        var accessToken = client.getAccessToken();

        // Verify that tokens are actually provided by the client
        if (accessToken == null) {
            var refreshToken = client.getRefreshToken();
            if (refreshToken != null) {
                return refreshAccessToken(refreshToken);
            }
            return AuthenticationOutcome.skip("MISSING_ACCESS_TOKEN");
        }

        try {
            // Attempt to parse the Access Token
            Claims claims = tokenUtils.parseAccessToken(accessToken);

            var tokenType = claims.get(JwtTokenUtils.TOKEN_TYPE_CLAIM);
            if (!JwtTokenUtils.ACCESS_TOKEN_TYPE.equals(tokenType)) {
                return AuthenticationOutcome.failure("INVALID_ACCESS_TOKEN");
            }

            return successOutcome(claims.getSubject(), claims);

        } catch (ExpiredJwtException e) {
            var refreshToken = client.getRefreshToken();
            if (refreshToken != null) {
                return refreshAccessToken(refreshToken);
            }
        } catch (Exception e) {
            return AuthenticationOutcome.failure("INVALID_ACCESS_TOKEN");
        }
    }

    /**
     * Logic for Refresh Token validation and token pair reissue.
     */
    protected AuthenticationOutcome refreshAccessToken(String refreshToken) {


        // Check for the physical presence of the Refresh Token
        if (refreshToken == null) {
            client.getTokens()
            return AuthenticationOutcome.stage();
        }

        try {
            // Validate the Refresh Token (throws ExpiredJwtException if expired)
            Claims refreshClaims = tokenUtils.parseRefreshToken(tokens.refreshToken());

            // Extract user data to generate new tokens
            String subject = refreshClaims.getSubject();
            Map<String, Object> claimsMap = new HashMap<>(refreshClaims);

            // Remove JWT metadata claims to prevent duplication when generating the new token
            claimsMap.remove(Claims.ISSUED_AT);
            claimsMap.remove(Claims.EXPIRATION);
            claimsMap.remove(JwtTokenUtils.TOKEN_TYPE_CLAIM);

            // Generate a new token pair (Token Rotation)
            String newAccessToken = tokenUtils.createAccessToken(subject, claimsMap);
            String newRefreshToken = tokenUtils.createRefreshToken(subject, claimsMap);

            // Save new tokens to the client (cookies / headers)
            client.setTokens(newAccessToken, newRefreshToken);

            // Parse the new access token to obtain updated Claims for the current Identity
            Claims newAccessClaims = tokenUtils.parseAccessToken(newAccessToken);
            return successOutcome(newAccessClaims);

        } catch (ExpiredJwtException ex) {
            // Refresh token has also expired
            return AuthenticationOutcome.failure("RefreshTokenExpired");
        } catch (Exception ex) {
            // Token is modified, forged, or otherwise invalid
            return AuthenticationOutcome.failure("InvalidRefreshToken");
        }
    }

    protected AuthenticationOutcome releaseTokens(String subject, Map<String, Object> claims) {
        String accessToken = tokenUtils.createAccessToken(subject, claims);
        String refreshToken = tokenUtils.createRefreshToken(subject);
        client.setTokens(accessToken, refreshToken);
        return successOutcome(subject, claims);
    }


    protected AuthenticationOutcome successOutcome(String subject, Map<String, Object> claims) {
        Map<String, Object> identityClaims = new HashMap<>(claims);
        identityClaims.put(Identity.AUTHENTICATOR_CLAIM, JwtAuthenticator.class);
        Identity identity = Identity.Default.of(subject, identityClaims);
        return AuthenticationOutcome.success(identity);
    }

    @Override
    public void logout(LogoutMessage message) {
        // Clear tokens from the client during logout as a security best practice
        client.setTokens(null, null);
    }
}