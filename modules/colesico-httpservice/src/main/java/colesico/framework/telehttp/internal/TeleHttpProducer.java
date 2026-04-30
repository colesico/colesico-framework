package colesico.framework.telehttp.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.OriginFactory;
import colesico.framework.telehttp.readwrite.principal.PrincipalHttpConfigPrototype;
import colesico.framework.telehttp.readwrite.profile.ProfileHttpConfigPrototype;
import colesico.framework.telehttp.assist.CSRFProtector;

import jakarta.inject.Singleton;

@Producer
@Produce(CSRFProtector.class)
@Produce(OriginFactory.class)
public class TeleHttpProducer {

    // Default config
    @Singleton
    public PrincipalHttpConfigPrototype principalWriterConfig() {
        return new PrincipalHttpConfigPrototype() {};
    }

    // Default config
    @Singleton
    public ProfileHttpConfigPrototype profileWriterConfig() {
        return new ProfileHttpConfigPrototype() {};
    }
}
