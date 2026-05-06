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

package colesico.framework.weblet.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.security.Principal;
import colesico.framework.telehttp.reader.PrincipalReader;
import colesico.framework.telehttp.reader.ProfileReader;
import colesico.framework.telehttp.reader.*;
import colesico.framework.weblet.teleapi.WebletTeleReader;
import colesico.framework.weblet.teleapi.reader.WebletReaderProxy;

import jakarta.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.OptionalInt;
import java.util.OptionalLong;

@Producer
public class WebletReadersProducer {

    //
    // Basic readers
    //

    @Singleton
    @Classed(Boolean.class)
    public WebletTeleReader booleanReader(BooleanReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(boolean.class)
    public WebletTeleReader boolReader(BooleanReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(String.class)
    public WebletTeleReader stringReader(StringReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Character.class)
    public WebletTeleReader characterReader(CharacterReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(char.class)
    public WebletTeleReader charReader(CharacterReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Byte.class)
    public WebletTeleReader byteReader(ByteReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(byte.class)
    public WebletTeleReader btReader(ByteReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Short.class)
    public WebletTeleReader shortReader(ShortReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(short.class)
    public WebletTeleReader shtReader(ShortReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Integer.class)
    public WebletTeleReader integerReader(IntegerReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(int.class)
    public WebletTeleReader intReader(IntegerReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalInt.class)
    public WebletTeleReader optionalIntReader(OptionalIntReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Long.class)
    public WebletTeleReader longReader(LongReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(long.class)
    public WebletTeleReader lngReader(LongReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(OptionalLong.class)
    public WebletTeleReader optionalLongReader(OptionalLongReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Float.class)
    public WebletTeleReader floatReader(FloatReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(float.class)
    public WebletTeleReader fltReader(FloatReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Double.class)
    public WebletTeleReader doubleReader(DoubleReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(double.class)
    public WebletTeleReader dblReader(DoubleReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Date.class)
    public WebletTeleReader dateReader(DateReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDate.class)
    public WebletTeleReader localDateReader(LocalDateReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalTime.class)
    public WebletTeleReader localTimeReader(LocalTimeReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public WebletTeleReader localDateTimeReader(LocalDateTimeReader impl) {
        return WebletReaderProxy.of(impl);
    }

    //
    // Extra readers
    //

    @Singleton
    @Classed(Profile.class)
    public WebletTeleReader profileReader(ProfileReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(Principal.class)
    public WebletTeleReader principalReader(PrincipalReader impl) {
        return WebletReaderProxy.of(impl);
    }

    @Singleton
    @Classed(HttpFileReader.class)
    public WebletTeleReader httpFileReader(HttpFileReader impl) {
        return WebletReaderProxy.of(impl);
    }

}
