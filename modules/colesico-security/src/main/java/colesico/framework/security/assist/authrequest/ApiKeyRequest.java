package colesico.framework.security.assist.authrequest;

import colesico.framework.security.authentication.AuthenticationRequest;

/**
 * Simple API Key credentials
 */
public record ApiKeyRequest(
        String apiKey  // Сам секретный ключ
) implements AuthenticationRequest {

    public static ApiKeyRequest of(String apiKey) {
        return new ApiKeyRequest(apiKey);
    }
}
