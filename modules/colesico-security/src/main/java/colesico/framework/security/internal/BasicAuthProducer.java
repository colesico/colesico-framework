package colesico.framework.security.internal;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.security.assist.authentication.basic.BasicMessage;
import colesico.framework.security.assist.authentication.basic.*;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Producer
@Produce(DefaultBasicSource.class)
@Produce(value = BasicAuth.class, scoped = Singleton.class)
public class BasicAuthProducer {

    @Singleton
    @Classed(BasicMessage.class)
    @Polyproduce(order = Integer.MAX_VALUE)
    public Authenticator authenticator(Provider<BasicAuth> impl) {
        return impl.get();
    }

    @Singleton
    @Substitute(Substitution.STUB)
    public BasicConfigPrototype config() {
        return new BasicConfigPrototype() {
        };
    }

    @Singleton
    @Substitute(Substitution.STUB)
    public BasicAccounts accountStorage(DefaultBasicAccounts impl) {
        return impl;
    }

}
