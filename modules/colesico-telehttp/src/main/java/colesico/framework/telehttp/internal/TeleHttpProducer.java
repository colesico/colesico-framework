package colesico.framework.telehttp.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.OriginFactory;
import colesico.framework.telehttp.PrincipalHttpConfigPrototype;
import colesico.framework.telehttp.ProfileHttpConfigPrototype;
import colesico.framework.telehttp.assist.CSRFProtector;

import jakarta.inject.Singleton;
import java.nio.charset.StandardCharsets;

@Producer
@Produce(CSRFProtector.class)
@Produce(OriginFactory.class)
public class TeleHttpProducer {

    // Default config
    @Singleton
    public PrincipalHttpConfigPrototype getPrincipalWriterConfig() {
        return new PrincipalHttpConfigPrototype() {};
    }

    // Default config
    @Singleton
    public ProfileHttpConfigPrototype getProfileWriterConfig() {
        return new ProfileHttpConfigPrototype() {};
    }
}
