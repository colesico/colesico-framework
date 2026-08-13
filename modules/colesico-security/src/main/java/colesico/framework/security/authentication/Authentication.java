package colesico.framework.security.authentication;

/**
 * Authentication flow API
 */
public interface Authentication {

    AuthenticationOutcome start();

    AuthenticationOutcome proceed(AuthenticationMessage message);

}
