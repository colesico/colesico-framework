package colesico.framework.telehttp.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.authentication.HttpBasic;
import colesico.framework.telehttp.origin.OriginFactory;
import colesico.framework.telehttp.writer.ProfileWriterConfigPrototype;
import colesico.framework.telehttp.assist.CSRFProtector;

import jakarta.inject.Singleton;

@Producer
@Produce(CSRFProtector.class)
@Produce(OriginFactory.class)
@Produce(HttpBasic.class)
public class TeleHttpProducer {

    // Default config
    @Singleton
    public ProfileWriterConfigPrototype profileWriterConfig() {
        return new ProfileWriterConfigPrototype() {
        };
    }
}
