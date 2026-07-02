package colesico.framework.telehttp.internal;

import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.response.*;
import colesico.framework.telehttp.writer.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import jakarta.inject.Singleton;

@Producer
@Produce(RedirectWriter.class)
@Produce(ForwardWriter.class)
@Produce(ExceptionResponseWriter.class)
@Produce(ObjectResponseWriter.class)
@Produce(StringResponseWriter.class)
@Produce(BytesResponseWriter.class)
@Produce(ObjectWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, substitute = Substitution.STUB)
public class WritersProducer {

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
    @Classed(ExceptionResponse.class)
    public TeleHttpWriter exceptionResponseWriter(ExceptionResponseWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ObjectResponse.class)
    public TeleHttpWriter objectResponseWriter(ObjectResponseWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(StringResponse.class)
    public TeleHttpWriter stringResponseWriter(StringResponseWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(BytesResponse.class)
    public TeleHttpWriter bytesResponseWriter(BytesResponseWriter imp) {
        return imp;
    }

    /**
     * Default writer
     */
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

    // Default config
    @Singleton
    public ProfileWriterConfigPrototype profileWriterConfig() {
        return new ProfileWriterConfigPrototype() {
        };
    }
}
