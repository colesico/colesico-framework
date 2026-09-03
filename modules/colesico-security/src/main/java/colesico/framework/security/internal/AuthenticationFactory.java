package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.AuthenticationResultHandler;
import colesico.framework.security.authentication.Authenticator;
import jakarta.inject.Singleton;

@Singleton
public class AuthenticationFactory {

    private Ioc ioc;

    public AuthenticationFactory(Ioc ioc) {
        this.ioc = ioc;
    }

    public <H extends AuthenticationResultHandler> H getResultHandler(Class<H> handlerClass) {
        return ioc.instance(handlerClass);
    }

    public <A extends Authenticator> A getAuthenticator(Class<A> authenticatorClass, Class<?> classed) {

        if (classed == null) {
            return ioc.instance(authenticatorClass);
        }

        ClassedKey<A> key = new ClassedKey<>(authenticatorClass, classed);
        return ioc.instance(key);

    }
}
