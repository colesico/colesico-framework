package colesico.framework.restlet.internal;

import colesico.framework.http.HttpFile;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.restlet.teleapi.RestletReaderProxy;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.security.Principal;
import colesico.framework.weblet.teleapi.reader.*;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Producer
public class RestletReadersProducer {

    @Singleton
    @Classed(Boolean.class)
    public RestletTeleReader getBooleanReader(BooleanReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(String.class)
    public RestletTeleReader getStringReader(StringReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public RestletTeleReader getByteReader(ByteReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public RestletTeleReader getIntegerReader(IntegerReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Long.class)
    public RestletTeleReader getLongReader(LongReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Float.class)
    public RestletTeleReader getFloatReader(FloatReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Double.class)
    public RestletTeleReader getDoubleReader(DoubleReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Date.class)
    public RestletTeleReader getDateReader(DateReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(LocalDate.class)
    public RestletTeleReader getLocalDateReader(LocalDateReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(LocalTime.class)
    public RestletTeleReader getLocalTimeReader(LocalTimeReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public RestletTeleReader getLocalDateTimeReader(LocalDateTimeReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(HttpFile.class)
    public RestletTeleReader getHttpFileReader(HttpFileReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public RestletTeleReader getProfileReader(ProfileReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Principal.class)
    public RestletTeleReader getPrincipalReader(PrincipalReader impl) {
        return new RestletReaderProxy(impl);
    }

}
