package colesico.framework.telehttp.internal;

import colesico.framework.http.HttpFile;
import colesico.framework.ioc.production.Classed;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.reader.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.OptionalInt;
import java.util.OptionalLong;

@Producer
@Produce(BooleanReader.class)
@Produce(StringReader.class)
@Produce(CharacterReader.class)
@Produce(ByteReader.class)
@Produce(ShortReader.class)
@Produce(IntegerReader.class)
@Produce(OptionalIntReader.class)
@Produce(LongReader.class)
@Produce(OptionalLongReader.class)
@Produce(FloatReader.class)
@Produce(DoubleReader.class)
@Produce(DateReader.class)
@Produce(LocalDateReader.class)
@Produce(LocalTimeReader.class)
@Produce(LocalDateTimeReader.class)
@Produce(PrincipalReader.class)
@Produce(ProfileReader.class)
@Produce(HttpFileReader.class)
public class ReadersProducer {

    @Singleton
    @Classed(Boolean.class)
    public HttpTeleReader getBooleanReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(boolean.class)
    public HttpTeleReader getBoolReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(String.class)
    public HttpTeleReader getStringReader(StringReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public HttpTeleReader getCharacterReader(CharacterReader impl) {
        return impl;
    }

    @Singleton
    @Classed(char.class)
    public HttpTeleReader getCharReader(CharacterReader impl) {
        return impl;
    }


    @Singleton
    @Classed(Byte.class)
    public HttpTeleReader getByteReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(byte.class)
    public HttpTeleReader getBtReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public HttpTeleReader getShortReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(short.class)
    public HttpTeleReader getShtReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public HttpTeleReader getIntegerReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(int.class)
    public HttpTeleReader getIntReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalInt.class)
    public HttpTeleReader getOptionalIntReader(OptionalIntReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public HttpTeleReader getLongReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(long.class)
    public HttpTeleReader getLngReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalLong.class)
    public HttpTeleReader getOptionalLongReader(OptionalLongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Float.class)
    public HttpTeleReader getFloatReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(float.class)
    public HttpTeleReader getFltReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Double.class)
    public HttpTeleReader getDoubleReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(double.class)
    public HttpTeleReader getDblReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Date.class)
    public HttpTeleReader getDateReader(DateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDate.class)
    public HttpTeleReader getLocalDateReader(LocalDateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalTime.class)
    public HttpTeleReader getLocalTimeReader(LocalTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public HttpTeleReader getLocalDateTimeReader(LocalDateTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(HttpFile.class)
    public HttpTeleReader getHttpFileReader(HttpFileReader impl) {
        return impl;
    }

}
