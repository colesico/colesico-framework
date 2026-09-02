package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.security.authentication.Authenticator;
import jakarta.inject.Singleton;

@Singleton
public class AuthenticatorFactory {
    private Ioc ioc;

    public AuthenticatorFactory(Ioc ioc) {
        this.ioc = ioc;
    }

    public <A extends Authenticator> A get(Class<A> authenticatorClass, Class<?> classifier) {

        if (classifier == null) {
            return ioc.instance(authenticatorClass);
        }

        ClassedKey<A> key = new ClassedKey<>(authenticatorClass, classifier);
        return ioc.instance(key);

    }
}
