package colesico.framework.security.internal;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.security.assist.authentication.BasicAuthenticationRequest;
import colesico.framework.security.assist.authentication.simple.SimpleAccountStorage;
import colesico.framework.security.assist.authentication.simple.SimpleAuthConfigPrototype;
import colesico.framework.security.assist.authentication.simple.SimpleAuthSource;
import colesico.framework.security.assist.authentication.simple.SimpleAuthenticator;
import colesico.framework.security.authentication.Authenticator;
import jakarta.inject.Singleton;

@Producer
@Produce(SimpleAuthSource.class)
@Produce(SimpleAuthenticator.class)

public class SimpleAuthProducer {

    @Singleton
    @Classed(BasicAuthenticationRequest.class)
    @Polyproduce(order = Integer.MAX_VALUE)
    Authenticator authenticator(SimpleAuthenticator impl) {
        return impl;
    }

    @Singleton
    @Substitute(Substitution.STUB)
    SimpleAuthConfigPrototype config() {
        return new SimpleAuthConfigPrototype() {
        };
    }

    @Singleton
    @Substitute(Substitution.STUB)
    SimpleAccountStorage accountStorage() {
        return new SimpleAccountStorage() {
        };
    }
}
