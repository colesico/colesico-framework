package colesico.framework.telehttp.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Supplier;
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
@Produce(ObjectWriter.class)
@Produce(BytesResponseWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, substitute = Substitution.STUB)
@Produce(value = TextPlainSerializer.class, keyType = ValueSerializer.class, named = TextPlainSerializer.MIME_TYPE)
public class WritersProducer {
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

    /**
     * ValueSerializer factory
     */
    @Unscoped
    public ValueSerializer valueSerializer(@IocMessage String mimeType, Ioc ioc) {
        var key = new NamedKey<>(ValueSerializer.class, mimeType);
        return ioc.instance(key);
    }

    // Default config
    @Singleton
    public ProfileWriterConfigPrototype profileWriterConfig() {
        return new ProfileWriterConfigPrototype() {
        };
    }
}
