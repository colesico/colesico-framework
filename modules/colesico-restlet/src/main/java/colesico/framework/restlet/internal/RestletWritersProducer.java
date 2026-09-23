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
import colesico.framework.restlet.RestletWriter;
import colesico.framework.restlet.writer.JsonExceptionWriter;
import colesico.framework.restlet.writer.JsonObjectWriter;
import colesico.framework.restlet.writer.JsonProblemResultWriter;
import colesico.framework.restlet.writer.JsonValueResultWriter;
import colesico.framework.telehttp.result.ProblemHttpResult;
import colesico.framework.telehttp.result.ProblemResult;
import colesico.framework.telehttp.result.ValueHttpResult;
import colesico.framework.telehttp.result.ValueResult;
import colesico.framework.telehttp.writer.ExceptionWriter;

import jakarta.inject.Singleton;

@Producer
@Produce(JsonValueResultWriter.class)
@Produce(JsonObjectWriter.class)
@Produce(JsonProblemResultWriter.class)
@Produce(JsonExceptionWriter.class)
public class RestletWritersProducer {

    @Singleton
    @Classed(ValueHttpResult.class)
    public RestletWriter valueHttpRestletWriter(JsonValueResultWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ValueResult.class)
    public RestletWriter valueRestletWriter(JsonValueResultWriter imp) {
        return imp;
    }

    // Default writer for object
    @Singleton
    @Classed(Object.class)
    public RestletWriter objectWriter(JsonObjectWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ProblemHttpResult.class)
    public RestletWriter problemHttpRestletWriter(JsonProblemResultWriter imp) {
        return imp;
    }

    @Singleton
    @Classed(ProblemResult.class)
    public RestletWriter problemRestletWriter(JsonProblemResultWriter imp) {
        return imp;
    }

    // Default writer for exception
    @Singleton
    @Classed(Exception.class)
    public RestletWriter exceptionWriter(JsonExceptionWriter impl) {
        return impl;
    }

}
