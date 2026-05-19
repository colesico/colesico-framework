package colesico.framework.security.assist.authentication;

import colesico.framework.security.authentication.AuthenticationRequest;

/**
 * Simple API Key credentials
 */
public record ApiKeyAuthenticationRequest(
        String apiKey  // Сам секретный ключ
) implements AuthenticationRequest {

    public static ApiKeyAuthenticationRequest of(String apiKey) {
        return new ApiKeyAuthenticationRequest(apiKey);
    }
}
