/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.restlet.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.restlet.RestletReader;
import colesico.framework.restlet.reader.*;
import colesico.framework.telehttp.reader.ProfileReader;

import colesico.framework.telehttp.reader.*;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.OptionalInt;
import java.util.OptionalLong;

@Producer
@Produce(ObjectReader.class)
public class RestletReadersProducer {

    // Default general purpose reader impl
    @Singleton
    public ObjectReader valueReader(ObjectReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Boolean.class)
    public RestletReader booleanReader(BooleanReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(boolean.class)
    public RestletReader boolReader(BooleanReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(String.class)
    public RestletReader stringReader(StringReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public RestletReader byteReader(ByteReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(byte.class)
    public RestletReader btReader(ByteReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Short.class)
    public RestletReader shortReader(ShortReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(short.class)
    public RestletReader shtReader(ShortReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public RestletReader integerReader(IntegerReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(int.class)
    public RestletReader intReader(IntegerReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalInt.class)
    public RestletReader optionalIntegerReader(OptionalIntReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Long.class)
    public RestletReader longReader(LongReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(long.class)
    public RestletReader lngReader(LongReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalLong.class)
    public RestletReader optionalLongReader(OptionalLongReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Float.class)
    public RestletReader floatReader(FloatReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(float.class)
    public RestletReader fltReader(FloatReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Double.class)
    public RestletReader doubleReader(DoubleReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(double.class)
    public RestletReader dblReader(DoubleReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Date.class)
    public RestletReader dateReader(DateReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDate.class)
    public RestletReader localDateReader(LocalDateReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalTime.class)
    public RestletReader localTimeReader(LocalTimeReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public RestletReader localDateTimeReader(LocalDateTimeReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public RestletReader profileReader(ProfileReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(HttpFileReader.class)
    public RestletReader httpFileReader(HttpFileReader impl) {
        return RestletReaderProxy.of(impl);
    }

}
