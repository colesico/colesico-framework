package colesico.framework.telehttp.internal;

import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.profile.Profile;
import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.result.*;
import colesico.framework.telehttp.writer.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import jakarta.inject.Singleton;

@Producer
@Produce(NavigationResultWriter.class)
@Produce(ExceptionResponseWriter.class)
@Produce(ToStringValueResultWriter.class)
@Produce(StringResultWriter.class)
@Produce(BytesResultWriter.class)
@Produce(ToStringObjectWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, keyType = HttpWriter.class, classed = Profile.class, substitute = Substitution.STUB)
public class WritersProducer {

    @Singleton
    @Classed(ObjectResult.class)
    public HttpWriter objectResponseWriter(ToStringValueResultWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ExceptionResponse.class)
    public HttpWriter exceptionResponseWriter(ExceptionResponseWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(NavigationResult.class)
    public HttpWriter navigationResponseWriter(NavigationResultWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(StringResult.class)
    public HttpWriter stringResponseWriter(StringResultWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(BytesResult.class)
    public HttpWriter bytesResponseWriter(BytesResultWriter imp) {
        return imp;
    }

    // Default writer for object
    @Singleton
    @Classed(Object.class)
    public HttpWriter objectWriter(ToStringObjectWriter impl) {
        return impl;
    }

    // Default writer for exception
    @Singleton
    @Classed(Exception.class)
    public HttpWriter exceptionWriter(ExceptionWriter impl) {
        return impl;
    }

    // Default config
    @Singleton
    public ProfileWriterConfigPrototype profileWriterConfig() {
        return new ProfileWriterConfigPrototype() {
        };
    }
}
