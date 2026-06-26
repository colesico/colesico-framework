package colesico.framework.telehttp.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.response.BytesResponse;
import colesico.framework.telehttp.response.ForwardResponse;
import colesico.framework.telehttp.response.RedirectResponse;
import colesico.framework.telehttp.writer.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import jakarta.inject.Singleton;


@Producer
@Produce(RedirectWriter.class)
@Produce(ForwardWriter.class)
@Produce(ValueResponseWriter.class)
@Produce(ValueWriter.class)
@Produce(BytesResponseWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, substitute = Substitution.STUB)
@Produce(value = TextPlainSerializer.class, keyType = ValueSerializer.class, named = MediaType.TEXT_PLAIN)
public class WritersProducer {

    /**
     * Default writer
     */
    @Singleton
    @Classed(Object.class)
    public TeleHttpWriter objectWriter(ValueWriter impl) {
        return impl;
    }

    /**
     * ValueSerializer factory
     */
    @Unscoped
    public ValueSerializer valueSerializer(@IocMessage String mimeType, Ioc ioc) {
        var key = new NamedKey<>(ValueSerializer.class, mimeType);
        return ioc.instance(key);
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
    public TeleHttpWriter stringResponseWriter(ToStringWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(String.class)
    public TeleHttpWriter stringWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public TeleHttpWriter longWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(long.class)
    public TeleHttpWriter lngWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public TeleHttpWriter integerWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(int.class)
    public TeleHttpWriter intWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public TeleHttpWriter shortWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Byte.class)
    public TeleHttpWriter byteWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Boolean.class)
    public TeleHttpWriter booleanWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(boolean.class)
    public TeleHttpWriter boolWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(byte.class)
    public TeleHttpWriter btWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public TeleHttpWriter characterWriter(ValueWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(char.class)
    public TeleHttpWriter chrarWriter(ValueWriter impl) {
        return impl;
    }
}
