package colesico.framework.telehttp.internal;

import colesico.framework.telehttp.OriginFactory;
import colesico.framework.telehttp.PrincipalHttpConfigPrototype;
import colesico.framework.telehttp.ProfileHttpConfigPrototype;
import colesico.framework.telehttp.assist.CSRFProtector;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.internal.objectreader.ReadingSchemeFactory;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;

@Producer
@Produce(CSRFProtector.class)
@Produce(ReadingSchemeFactory.class)
@Produce(OriginFactory.class)
public class TeleHttpProducer {

    // Default config
    @Singleton
    public PrincipalHttpConfigPrototype getPrincipalWriterConfig() {
        return new PrincipalHttpConfigPrototype() {
            @Override
            public byte[] getSignatureKey() {
                return "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
            }
        };
    }

    // Default config
    @Singleton
    public ProfileHttpConfigPrototype getProfileWriterConfig() {
        return new ProfileHttpConfigPrototype() {
            @Override
            public int getCookieValidityDays() {
                return 365;
            }
        };
    }
}
