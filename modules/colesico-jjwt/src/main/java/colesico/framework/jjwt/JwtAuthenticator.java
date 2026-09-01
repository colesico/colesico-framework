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
        if (message instanceof JwtLoginMessage jim) {
            return releaseTokens(jim.subject(), jim.claims());
        }

        var accessToken = client.retrieveAccessToken();
        if (accessToken != null) {
            return checkAccessToken(accessToken);
        }

        // If Access Token is missing, check if we can reissue it using the Refresh Token
        var refreshToken = client.retrieveRefreshToken();
        if (refreshToken != null && !refreshToken.isBlank()) {
            return refreshAccessToken(refreshToken);
        }

        return AuthenticationOutcome.skip("NO_JWT_TOKEN");
    }

    protected AuthenticationOutcome checkAccessToken(String accessToken) {
        try {
            Claims claims = tokenUtils.parseAccessToken(accessToken);
            return successOutcome(claims.getSubject(), claims);
        } catch (ExpiredJwtException e) {
            var refreshToken = client.retrieveRefreshToken();
            return refreshAccessToken(refreshToken);
        } catch (Exception e) {
            return AuthenticationOutcome.failure("INVALID_ACCESS_TOKEN");
        }
    }

    /**
     * Logic for Refresh Token validation and token pair reissue.
     */
    protected AuthenticationOutcome refreshAccessToken(String refreshToken) {
        if (refreshToken == null) {
            return AuthenticationOutcome.stage();
        }

        try {
            // Validate the Refresh Token (throws ExpiredJwtException if expired)
            Claims refreshClaims = tokenUtils.parseRefreshToken(refreshToken);

            // Extract user data to generate new tokens
            String subject = refreshClaims.getSubject();

            // Populate claims
            Map<String, Object> claims = provideClaims(subject);
            if (claims == null) {
                return AuthenticationOutcome.failure("REFRESH_ACCESS_TOKEN_FAILURE");
            }

            var accessToken = tokenUtils.createAccessToken(subject, claims);

            // Save new access tokens and old refresh token to the client
            client.populateTokens(accessToken, refreshToken);

            return successOutcome(subject, claims);

        } catch (ExpiredJwtException ex) {
            // Refresh token has also expired
            return AuthenticationOutcome.failure("REFRESH_TOKEN_EXPIRED");
        } catch (Exception ex) {
            // Token is modified, forged, or otherwise invalid
            return AuthenticationOutcome.failure("INVALID_REFRESH_TOKEN");
        }
    }

    /**
     * Default implementation. Override and
     * return null to deny access token refresh
     */
    protected Map<String, Object> provideClaims(String subject) {
        return new HashMap<>();
    }

    protected AuthenticationOutcome releaseTokens(String subject, Map<String, Object> claims) {
        String accessToken = tokenUtils.createAccessToken(subject, claims);
        String refreshToken = tokenUtils.createRefreshToken(subject);
        client.populateTokens(accessToken, refreshToken);
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
        client.populateTokens(null, null);
    }
}