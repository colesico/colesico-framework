package colesico.framework.telehttp.internal;

import colesico.framework.http.HttpFile;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.profile.Profile;
import colesico.framework.telehttp.HttpReader;
import colesico.framework.telehttp.reader.ProfileReader;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

import colesico.framework.telehttp.reader.*;
import jakarta.inject.Singleton;

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
@Produce(value = ProfileReader.class, substitute = Substitution.STUB)
@Produce(HttpFileReader.class)
public class ReadersProducer {

    @Singleton
    @Classed(String.class)
    public HttpReader stringReader(StringReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public HttpReader characterReader(CharacterReader impl) {
        return impl;
    }

    @Singleton
    @Classed(char.class)
    public HttpReader charReader(CharacterReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Boolean.class)
    public HttpReader booleanReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(boolean.class)
    public HttpReader boolReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Byte.class)
    public HttpReader byteReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(byte.class)
    public HttpReader btReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public HttpReader shortReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(short.class)
    public HttpReader shtReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public HttpReader integerReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(int.class)
    public HttpReader intReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalInt.class)
    public HttpReader optionalIntReader(OptionalIntReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public HttpReader longReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(long.class)
    public HttpReader lngReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalLong.class)
    public HttpReader optionalLongReader(OptionalLongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Float.class)
    public HttpReader floatReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(float.class)
    public HttpReader fltReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Double.class)
    public HttpReader doubleReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(double.class)
    public HttpReader dblReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Date.class)
    public HttpReader dateReader(DateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDate.class)
    public HttpReader localDateReader(LocalDateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalTime.class)
    public HttpReader localTimeReader(LocalTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public HttpReader localDateTimeReader(LocalDateTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(HttpFile.class)
    public HttpReader httpFileReader(HttpFileReader impl) {
        return impl;
    }

}
