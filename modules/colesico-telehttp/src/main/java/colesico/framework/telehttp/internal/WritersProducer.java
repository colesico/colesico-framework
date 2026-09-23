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
@Produce(ValueHttpResultWriter.class)
@Produce(ProblemHttpResultWriter.class)
@Produce(StringResultWriter.class)
@Produce(BytesResultWriter.class)
@Produce(ObjectWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, keyType = HttpWriter.class, classed = Profile.class, substitute = Substitution.STUB)
public class WritersProducer {

    @Singleton
    @Classed(ValueHttpResult.class)
    public HttpWriter valueHttpResultWriter(ValueHttpResultWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ValueResult.class)
    public HttpWriter valueResultWriter(ValueHttpResultWriter imp) {
        return imp;
    }

    // Default writer for object
    @Singleton
    @Classed(Object.class)
    public HttpWriter objectWriter(ObjectWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ProblemHttpResult.class)
    public HttpWriter problemHttpResultWriter(ProblemHttpResultWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ProblemResult.class)
    public HttpWriter problemResultWriter(ProblemHttpResultWriter imp) {
        return imp;
    }

    // Default writer for exception
    @Singleton
    @Classed(Exception.class)
    public HttpWriter exceptionWriter(ExceptionWriter impl) {
        return impl;
    }


    @Singleton
    @Classed(NavigationResult.class)
    public HttpWriter navigationResultWriter(NavigationResultWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(StringResult.class)
    public HttpWriter stringResultWriter(StringResultWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(BytesResult.class)
    public HttpWriter bytesResultWriter(BytesResultWriter imp) {
        return imp;
    }


    // Default config
    @Singleton
    public ProfileWriterConfigPrototype profileWriterConfig() {
        return new ProfileWriterConfigPrototype() {
        };
    }
}
