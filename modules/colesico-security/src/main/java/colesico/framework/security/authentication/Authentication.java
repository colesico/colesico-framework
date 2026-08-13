package colesico.framework.security.authentication;

/**
 * Authentication flow API
 */
public interface Authentication {

    String id();

    AuthenticationOutcome start();

    AuthenticationOutcome proceed(AuthenticationMessage message);

}
