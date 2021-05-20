/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
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
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.restlet.teleapi.reader.*;
import colesico.framework.security.Principal;
import colesico.framework.telehttp.reader.*;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.OptionalInt;
import java.util.OptionalLong;

@Producer
@Produce(JsonReader.class)
@Produce(JsonEntryReader.class)
@Produce(RestletObjectReader.class)
public class RestletReadersProducer {

    // Default general purpose reader impl
    @Singleton
    public ValueReader getValueReader(JsonReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Boolean.class)
    public RestletTeleReader getBooleanReader(BooleanReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(boolean.class)
    public RestletTeleReader getBoolReader(BooleanReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(String.class)
    public RestletTeleReader getStringReader(StringReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public RestletTeleReader getByteReader(ByteReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(byte.class)
    public RestletTeleReader getBtReader(ByteReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Short.class)
    public RestletTeleReader getShortReader(ShortReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(short.class)
    public RestletTeleReader getShtReader(ShortReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public RestletTeleReader getIntegerReader(IntegerReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(int.class)
    public RestletTeleReader getIntReader(IntegerReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalInt.class)
    public RestletTeleReader getOptionalIntegerReader(OptionalIntReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Long.class)
    public RestletTeleReader getLongReader(LongReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(long.class)
    public RestletTeleReader getLngReader(LongReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalLong.class)
    public RestletTeleReader getOptionalLongReader(OptionalLongReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Float.class)
    public RestletTeleReader getFloatReader(FloatReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(float.class)
    public RestletTeleReader getFltReader(FloatReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Double.class)
    public RestletTeleReader getDoubleReader(DoubleReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(double.class)
    public RestletTeleReader getDblReader(DoubleReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Date.class)
    public RestletTeleReader getDateReader(DateReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDate.class)
    public RestletTeleReader getLocalDateReader(LocalDateReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalTime.class)
    public RestletTeleReader getLocalTimeReader(LocalTimeReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public RestletTeleReader getLocalDateTimeReader(LocalDateTimeReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public RestletTeleReader getProfileReader(ProfileReader impl) {
        return RestletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Principal.class)
    public RestletTeleReader getPrincipalReader(PrincipalReader impl) {
        return RestletReaderProxy.of(impl);
    }

}
