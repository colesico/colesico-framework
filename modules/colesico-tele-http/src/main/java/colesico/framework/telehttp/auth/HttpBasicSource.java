package colesico.framework.telehttp.auth;

import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;

public class HttpBasicSource implements AuthenticationSource {

    @Override
    public AuthenticationRequest request() {
        return null;
    }

    @Override
    public <C extends AuthenticationChallenge> void proceed(C challenge) {

    }
}
