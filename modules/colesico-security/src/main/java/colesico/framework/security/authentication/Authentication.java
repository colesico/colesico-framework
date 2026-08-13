package colesico.framework.security.authentication;

public interface Authentication {

    AuthenticationOutcome start();

    AuthenticationOutcome proceed(AuthenticationMessage message);

}
