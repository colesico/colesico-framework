package colesico.framework.security.internal;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.security.assist.authentication.basic.BasicRequest;
import colesico.framework.security.assist.authentication.basic.*;
import colesico.framework.security.authentication.Authenticator;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Producer
@Produce(BasicSource.class)
@Produce(value = BasicAuthenticator.class, scoped = Singleton.class)
public class BasicAuthProducer {

    @Singleton
    @Classed(BasicRequest.class)
    @Polyproduce(order = Integer.MAX_VALUE)
    public Authenticator authenticator(Provider<BasicAuthenticator> impl) {
        return impl.get();
    }

    @Singleton
    @Substitute(Substitution.STUB)
    public BasicAuthConfigPrototype config() {
        return new BasicAuthConfigPrototype() {
        };
    }

    @Singleton
    @Substitute(Substitution.STUB)
    public BasicAccountStorage accountStorage(DefaultBasicAccountStorage impl) {
        return impl;
    }

}
