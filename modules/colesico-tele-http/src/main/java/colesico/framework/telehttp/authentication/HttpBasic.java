package colesico.framework.telehttp.authentication;

import colesico.framework.http.HttpRequest;
import colesico.framework.security.Identity;
import colesico.framework.security.assist.authentication.BasicAuthenticationChallenge;
import colesico.framework.security.assist.authentication.BasicAuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class HttpBasic implements AuthenticationSource<BasicAuthenticationRequest, BasicAuthenticationChallenge> {

    private final Provider<HttpRequest> httpRequest;

    public HttpBasic(Provider<HttpRequest> httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public BasicAuthenticationRequest request() {
        return null;
    }

    @Override
    public void proceed(BasicAuthenticationChallenge challenge) {
        AuthenticationSource.super.proceed(challenge);
    }

    @Override
    public void authenticate(Identity<?> identity) {
        AuthenticationSource.super.authenticate(identity);
    }

    @Override
    public void unauthenticated(BasicAuthenticationRequest request) {
        AuthenticationSource.super.unauthenticated(request);
    }

    @Override
    public void logout(Identity<?> identity) {
        AuthenticationSource.super.logout(identity);
    }
}
