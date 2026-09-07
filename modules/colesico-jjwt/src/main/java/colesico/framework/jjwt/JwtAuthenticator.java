package colesico.framework.jjwt;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.IssuedIdentity;
import colesico.framework.security.authentication.AuthenticationOutcome;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.LogoutMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

abstract public class JwtAuthenticator implements Authenticator<JwtMessage, LogoutMessage> {

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

        var accessToken = client.readAccessToken();
        if (accessToken != null) {
            return checkAccessToken(accessToken);
        }

        // If Access Token is missing, check if Refresh Token was passed
        var refreshToken = client.readRefreshToken();
        if (refreshToken != null) {
            return refreshAccessToken(refreshToken);
        }

        // Neither Access nor Refresh tokens are available.
        return AuthenticationOutcome.bypass("NoJwtTokens");
    }

    protected AuthenticationOutcome checkAccessToken(String accessToken) {
        try {
            Claims claims = tokenUtils.parseAccessToken(accessToken);
            return successOutcome(claims.getSubject(), claims);
        } catch (ExpiredJwtException e) {
            // Access token expired.
            // Attempt an immediate inline refresh using the available Refresh Token.
            var refreshToken = client.readRefreshToken();
            if (refreshToken != null) {
                return refreshAccessToken(refreshToken);
            }
            // No refresh token available to rescue the expired access token
            client.askRefreshToken();
            return AuthenticationOutcome.stage("RefreshTokenRequested");
        } catch (Exception e) {
            client.clearTokens();
            return AuthenticationOutcome.failure("InvalidAccessToken");
        }
    }

    /**
     * Logic for Refresh Token validation and token pair reissue.
     */
    protected AuthenticationOutcome refreshAccessToken(String refreshToken) {

        try {
            // Validate the Refresh Token (throws ExpiredJwtException if expired)
            Claims refreshClaims = tokenUtils.parseRefreshToken(refreshToken);

            // Extract user data to generate new tokens
            String subject = refreshClaims.getSubject();

            // Populate claims
            Map<String, Object> claims = provideClaims(subject);
            if (claims == null) {
                client.clearTokens();
                return AuthenticationOutcome.failure("RefreshAccessTokenFailure");
            }

            var accessToken = tokenUtils.createAccessToken(subject, claims);

            // Save new access tokens and old refresh token to the client
            client.populateTokens(accessToken, refreshToken);

            return successOutcome(subject, claims);

        } catch (ExpiredJwtException ex) {
            // Refresh token has also expired
            client.clearTokens();
            return AuthenticationOutcome.failure("RefreshTokenExpired");
        } catch (Exception ex) {
            // Token is modified, forged, or otherwise invalid
            client.clearTokens();
            return AuthenticationOutcome.failure("InvalidRefreshToken");
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
        Identity identity = IssuedIdentity.of(this, subject, claims);
        return AuthenticationOutcome.success(identity);
    }

    @Override
    public void logout(LogoutMessage message) {
        client.clearTokens();
    }
}