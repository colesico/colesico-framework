package colesico.framework.weblet.internal;

import colesico.framework.http.HttpFile;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.profile.Profile;
import colesico.framework.security.Principal;
import colesico.framework.weblet.teleapi.WebletTeleReader;
import colesico.framework.weblet.teleapi.reader.*;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Producer(Rank.RANK_MINOR)
@Produce(BooleanReader.class)
@Produce(StringReader.class)
@Produce(CharacterReader.class)
@Produce(ByteReader.class)
@Produce(ShortReader.class)
@Produce(IntegerReader.class)
@Produce(LongReader.class)
@Produce(FloatReader.class)
@Produce(DoubleReader.class)
@Produce(DateReader.class)
@Produce(LocalDateReader.class)
@Produce(LocalTimeReader.class)
@Produce(LocalDateTimeReader.class)
@Produce(HttpFileReader.class)
@Produce(PrincipalReader.class)
@Produce(ProfileReader.class)
public class WebletReadersProducer {

    @Singleton
    @Classed(Boolean.class)
    public WebletTeleReader getBooleanReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(String.class)
    public WebletTeleReader getStringReader(StringReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public WebletTeleReader getCharacterReader(CharacterReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Byte.class)
    public WebletTeleReader getByteReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public WebletTeleReader getShortReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public WebletTeleReader getIntegerReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public WebletTeleReader getLongReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Float.class)
    public WebletTeleReader getFloatReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Double.class)
    public WebletTeleReader getDoubleReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Date.class)
    public WebletTeleReader getDateReader(DateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDate.class)
    public WebletTeleReader getLocalDateReader(LocalDateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalTime.class)
    public WebletTeleReader getLocalTimeReader(LocalTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public WebletTeleReader getLocalDateTimeReader(LocalDateTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(HttpFile.class)
    public WebletTeleReader getHttpFileReader(HttpFileReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public WebletTeleReader getProfileReader(ProfileReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Principal.class)
    public WebletTeleReader getPrincipalReader(PrincipalReader impl) {
        return impl;
    }

}
