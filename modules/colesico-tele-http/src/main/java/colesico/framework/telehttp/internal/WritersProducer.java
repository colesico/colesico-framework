package colesico.framework.telehttp.internal;

import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.profile.Profile;
import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.response.*;
import colesico.framework.telehttp.writer.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import jakarta.inject.Singleton;

@Producer
@Produce(RedirectWriter.class)
@Produce(ForwardWriter.class)
@Produce(ExceptionResponseWriter.class)
@Produce(ToStringObjectResponseWriter.class)
@Produce(StringResponseWriter.class)
@Produce(BytesResponseWriter.class)
@Produce(ToStringObjectWriter.class)
@Produce(ExceptionWriter.class)
//@Produce(value = ProfileWriter.class, keyType = HttpWriter.class, classed = Profile.class, substitute = Substitution.STUB)
public class WritersProducer {

    @Singleton
    @Classed(RedirectResponse.class)
    public HttpWriter redirectResponseWriter(RedirectWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(ForwardResponse.class)
    public HttpWriter forwardResponseWriter(ForwardWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(ExceptionResponse.class)
    public HttpWriter exceptionResponseWriter(ExceptionResponseWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ObjectResponse.class)
    public HttpWriter objectResponseWriter(ToStringObjectResponseWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(StringResponse.class)
    public HttpWriter stringResponseWriter(StringResponseWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(BytesResponse.class)
    public HttpWriter bytesResponseWriter(BytesResponseWriter imp) {
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
