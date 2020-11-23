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

package colesico.framework.weblet.internal;

import colesico.framework.http.HttpFile;
import colesico.framework.telehttp.reader.*;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.security.Principal;
import colesico.framework.weblet.teleapi.WebletTeleReader;
import colesico.framework.weblet.teleapi.reader.HttpFileReader;
import colesico.framework.weblet.teleapi.reader.WebletReaderProxy;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.OptionalInt;
import java.util.OptionalLong;

@Producer
@Produce(HttpFileReader.class)
public class WebletReadersProducer {

    //
    // Basic readers
    //

    @Singleton
    @Classed(Object.class)
    public WebletTeleReader getObjectReader(ObjectReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Boolean.class)
    public WebletTeleReader getBooleanReader(BooleanReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(String.class)
    public WebletTeleReader getStringReader(StringReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Character.class)
    public WebletTeleReader getCharacterReader(CharacterReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public WebletTeleReader getByteReader(ByteReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Short.class)
    public WebletTeleReader getShortReader(ShortReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public WebletTeleReader getIntegerReader(IntegerReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalInt.class)
    public WebletTeleReader getOptionalIntReader(OptionalIntReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Long.class)
    public WebletTeleReader getLongReader(LongReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalLong.class)
    public WebletTeleReader getOptionalLongReader(OptionalLongReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Float.class)
    public WebletTeleReader getFloatReader(FloatReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Double.class)
    public WebletTeleReader getDoubleReader(DoubleReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Date.class)
    public WebletTeleReader getDateReader(DateReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDate.class)
    public WebletTeleReader getLocalDateReader(LocalDateReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalTime.class)
    public WebletTeleReader getLocalTimeReader(LocalTimeReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public WebletTeleReader getLocalDateTimeReader(LocalDateTimeReader impl) {
        return WebletReaderProxy.of(impl);
    }

    //
    // Extra readers
    //

    @Singleton
    @Classed(Profile.class)
    public WebletTeleReader getProfileReader(ProfileReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Principal.class)
    public WebletTeleReader getPrincipalReader(PrincipalReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(HttpFile.class)
    public WebletTeleReader getHttpFileReader(HttpFileReader impl) {
        return impl;
    }

}
