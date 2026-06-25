package colesico.framework.telehttp.internal;

import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.response.BinaryResponse;
import colesico.framework.telehttp.response.ForwardResponse;
import colesico.framework.telehttp.response.RedirectResponse;
import colesico.framework.telehttp.response.StringResponse;
import colesico.framework.telehttp.writer.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import jakarta.inject.Singleton;


@Producer
@Produce(StringifyWriter.class)
@Produce(value = RedirectWriter.class, keyType = TeleHttpWriter.class, classed = RedirectResponse.class)
@Produce(value = ForwardWriter.class, keyType = TeleHttpWriter.class, classed = ForwardResponse.class)
@Produce(value = StringResponseWriter.class, keyType = TeleHttpWriter.class, classed = StringResponse.class)
@Produce(value = BinaryResponseWriter.class, keyType = TeleHttpWriter.class, classed = BinaryResponse.class)
@Produce(value = ExceptionWriter.class, keyType = TeleHttpWriter.class, classed = Exception.class)
@Produce(value = ProfileWriter.class, substitute = Substitution.STUB)
public class WritersProducer {

    @Singleton
    @Classed(String.class)
    public TeleHttpWriter stringWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public TeleHttpWriter longWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(long.class)
    public TeleHttpWriter lngWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public TeleHttpWriter integerWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(int.class)
    public TeleHttpWriter intWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public TeleHttpWriter shortWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Byte.class)
    public TeleHttpWriter byteWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Boolean.class)
    public TeleHttpWriter booleanWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(boolean.class)
    public TeleHttpWriter boolWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(byte.class)
    public TeleHttpWriter btWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public TeleHttpWriter characterWriter(StringifyWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(char.class)
    public TeleHttpWriter chrarWriter(StringifyWriter impl) {
        return impl;
    }
}
