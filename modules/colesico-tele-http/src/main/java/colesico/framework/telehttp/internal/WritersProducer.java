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
@Produce(RedirectWriter.class)
@Produce(ForwardWriter.class)
@Produce(StringResponseWriter.class)
@Produce(BinaryResponseWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, substitute = Substitution.STUB)
public class WritersProducer {

    @Singleton
    @Classed(Exception.class)
    public TeleHttpWriter exceptionWriter(ExceptionWriter impl) {
        return impl;
    }


    @Singleton
    @Classed(RedirectResponse.class)
    public TeleHttpWriter redirectResponseWriter(RedirectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(ForwardResponse.class)
    public TeleHttpWriter forwardResponseWriter(ForwardWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(BinaryResponse.class)
    public TeleHttpWriter binaryResponseWriter(BinaryResponseWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(StringResponse.class)
    public TeleHttpWriter stringResponseWriter(StringResponseWriter impl) {
        return impl;
    }

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
