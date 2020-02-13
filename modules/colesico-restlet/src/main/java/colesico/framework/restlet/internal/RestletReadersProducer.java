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

import colesico.framework.http.HttpFile;
import colesico.framework.ioc.annotation.Classed;
import colesico.framework.ioc.annotation.Producer;
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
import java.util.OptionalInt;
import java.util.OptionalLong;



@Producer(MinorTag.class)
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
    @Classed(OptionalInt.class)
    public RestletTeleReader getOptionalIntegerReader(OptionalIntReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(Long.class)
    public RestletTeleReader getLongReader(LongReader impl) {
        return new RestletReaderProxy(impl);
    }

    @Singleton
    @Classed(OptionalLong.class)
    public RestletTeleReader getOptionalLongReader(OptionalLongReader impl) {
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
