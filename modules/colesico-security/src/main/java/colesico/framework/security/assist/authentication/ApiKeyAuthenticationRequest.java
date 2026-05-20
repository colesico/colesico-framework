package colesico.framework.security.assist.authentication;

import colesico.framework.security.authentication.AuthenticationRequest;

import java.util.Map;

/**
 * Simple API Key credentials
 */
public record ApiKeyAuthenticationRequest(
        String apiKey,
        Map<String, Object> claims
) implements AuthenticationRequest {

    public static ApiKeyAuthenticationRequest of(String apiKey, Map<String, Object> claims) {
        return new ApiKeyAuthenticationRequest(apiKey, claims);
    }
}
