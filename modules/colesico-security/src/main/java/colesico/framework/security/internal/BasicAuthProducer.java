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
@Produce(BasicAuth.class)
@Produce(value = DefaultBasicSource.class, substitute = Substitution.STUB)
public class BasicAuthProducer {

    /**
     * Default config
     */
    @Singleton
    @Substitute(Substitution.STUB)
    public BasicConfigPrototype config() {
        return new BasicConfigPrototype() {
        };
    }

    /**
     * Default accounts storage
     */
    @Singleton
    @Substitute(Substitution.STUB)
    public BasicAccounts accountStorage(DefaultBasicAccounts impl) {
        return impl;
    }

}
