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
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.restlet.teleapi.RestletReaderProxy;
import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.security.Principal;
import colesico.framework.weblet.teleapi.WebletTRContext;
import colesico.framework.weblet.teleapi.reader.*;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;

@Producer
public class RestletReadersProducer {

    public static final Function<RestletTRContext, WebletTRContext>
            ctxConverter = c -> new WebletTRContext(c.getName(), c.getOriginFacade());

    @Singleton
    @Classed(Boolean.class)
    public RestletTeleReader getBooleanReader(BooleanReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(String.class)
    public RestletTeleReader getStringReader(StringReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Byte.class)
    public RestletTeleReader getByteReader(ByteReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Short.class)
    public RestletTeleReader getShortReader(ShortReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Integer.class)
    public RestletTeleReader getIntegerReader(IntegerReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(OptionalInt.class)
    public RestletTeleReader getOptionalIntegerReader(OptionalIntReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Long.class)
    public RestletTeleReader getLongReader(LongReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(OptionalLong.class)
    public RestletTeleReader getOptionalLongReader(OptionalLongReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Float.class)
    public RestletTeleReader getFloatReader(FloatReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Double.class)
    public RestletTeleReader getDoubleReader(DoubleReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Date.class)
    public RestletTeleReader getDateReader(DateReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(LocalDate.class)
    public RestletTeleReader getLocalDateReader(LocalDateReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(LocalTime.class)
    public RestletTeleReader getLocalTimeReader(LocalTimeReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(LocalDateTime.class)
    public RestletTeleReader getLocalDateTimeReader(LocalDateTimeReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(HttpFile.class)
    public RestletTeleReader getHttpFileReader(HttpFileReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Profile.class)
    public RestletTeleReader getProfileReader(ProfileReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

    @Singleton
    @Classed(Principal.class)
    public RestletTeleReader getPrincipalReader(PrincipalReader impl) {
        return new RestletReaderProxy(impl, ctxConverter);
    }

}
