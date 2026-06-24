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
    @Classed(Boolean.class)
    public TeleHttpReader getBooleanReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(boolean.class)
    public TeleHttpReader getBoolReader(BooleanReader impl) {
        return impl;
    }

    @Singleton
    @Classed(String.class)
    public TeleHttpReader getStringReader(StringReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Character.class)
    public TeleHttpReader getCharacterReader(CharacterReader impl) {
        return impl;
    }

    @Singleton
    @Classed(char.class)
    public TeleHttpReader getCharReader(CharacterReader impl) {
        return impl;
    }


    @Singleton
    @Classed(Byte.class)
    public TeleHttpReader getByteReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(byte.class)
    public TeleHttpReader getBtReader(ByteReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Short.class)
    public TeleHttpReader getShortReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(short.class)
    public TeleHttpReader getShtReader(ShortReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Integer.class)
    public TeleHttpReader getIntegerReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(int.class)
    public TeleHttpReader getIntReader(IntegerReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalInt.class)
    public TeleHttpReader getOptionalIntReader(OptionalIntReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Long.class)
    public TeleHttpReader getLongReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(long.class)
    public TeleHttpReader getLngReader(LongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(OptionalLong.class)
    public TeleHttpReader getOptionalLongReader(OptionalLongReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Float.class)
    public TeleHttpReader getFloatReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(float.class)
    public TeleHttpReader getFltReader(FloatReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Double.class)
    public TeleHttpReader getDoubleReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(double.class)
    public TeleHttpReader getDblReader(DoubleReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Date.class)
    public TeleHttpReader getDateReader(DateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDate.class)
    public TeleHttpReader getLocalDateReader(LocalDateReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalTime.class)
    public TeleHttpReader getLocalTimeReader(LocalTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public TeleHttpReader getLocalDateTimeReader(LocalDateTimeReader impl) {
        return impl;
    }

    @Singleton
    @Classed(HttpFile.class)
    public TeleHttpReader getHttpFileReader(HttpFileReader impl) {
        return impl;
    }

}
