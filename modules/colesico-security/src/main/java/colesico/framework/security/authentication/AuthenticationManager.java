package colesico.framework.security.authentication;

/**
 * Iterates over AuthenticationProviders
 */
public interface AuthenticationManager {
    AuthenticationResult<?> authenticate(AuthenticationContext context);
}
