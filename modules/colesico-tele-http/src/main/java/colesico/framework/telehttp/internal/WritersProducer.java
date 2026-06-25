package colesico.framework.telehttp.internal;

import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.response.BytesResponse;
import colesico.framework.telehttp.response.ForwardResponse;
import colesico.framework.telehttp.response.RedirectResponse;
import colesico.framework.telehttp.response.StringResponse;
import colesico.framework.telehttp.writer.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import jakarta.inject.Singleton;


@Producer
@Produce(ObjectWriter.class)
@Produce(RedirectWriter.class)
@Produce(ForwardWriter.class)
@Produce(StringResponseWriter.class)
@Produce(BytesResponseWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, substitute = Substitution.STUB)
public class WritersProducer {

    @Singleton
    @Classed(Object.class)
    public TeleHttpWriter objectWriter(ObjectWriter impl) {
        return impl;
    }

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
    @Classed(BytesResponse.class)
    public TeleHttpWriter binaryResponseWriter(BytesResponseWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(StringResponse.class)
    public TeleHttpWriter stringResponseWriter(StringResponseWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(String.class)
    public TeleHttpWriter stringWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public TeleHttpWriter longWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(long.class)
    public TeleHttpWriter lngWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public TeleHttpWriter integerWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(int.class)
    public TeleHttpWriter intWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public TeleHttpWriter shortWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Byte.class)
    public TeleHttpWriter byteWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Boolean.class)
    public TeleHttpWriter booleanWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(boolean.class)
    public TeleHttpWriter boolWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(byte.class)
    public TeleHttpWriter btWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public TeleHttpWriter characterWriter(ObjectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(char.class)
    public TeleHttpWriter chrarWriter(ObjectWriter impl) {
        return impl;
    }
}
