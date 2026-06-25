package colesico.framework.telehttp.internal;

import colesico.framework.http.HttpFile;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.telehttp.TeleHttpReader;
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
    public TeleHttpReader stringReader(StringReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public TeleHttpReader characterReader(CharacterReader impl) {
        return impl;
    }

    @Singleton
    @Classed(char.class)
    public TeleHttpReader charReader(CharacterReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Boolean.class)
    public TeleHttpReader booleanReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(boolean.class)
    public TeleHttpReader boolReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Byte.class)
    public TeleHttpReader byteReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(byte.class)
    public TeleHttpReader btReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public TeleHttpReader shortReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(short.class)
    public TeleHttpReader shtReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public TeleHttpReader integerReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(int.class)
    public TeleHttpReader intReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalInt.class)
    public TeleHttpReader optionalIntReader(OptionalIntReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public TeleHttpReader longReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(long.class)
    public TeleHttpReader lngReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalLong.class)
    public TeleHttpReader optionalLongReader(OptionalLongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Float.class)
    public TeleHttpReader floatReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(float.class)
    public TeleHttpReader fltReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Double.class)
    public TeleHttpReader doubleReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(double.class)
    public TeleHttpReader dblReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Date.class)
    public TeleHttpReader dateReader(DateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDate.class)
    public TeleHttpReader localDateReader(LocalDateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalTime.class)
    public TeleHttpReader localTimeReader(LocalTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public TeleHttpReader localDateTimeReader(LocalDateTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(HttpFile.class)
    public TeleHttpReader httpFileReader(HttpFileReader impl) {
        return impl;
    }

}
