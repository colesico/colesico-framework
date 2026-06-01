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

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import colesico.framework.weblet.response.StringResponse;
import colesico.framework.weblet.response.TextResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class StringWriter
        extends TeleHttpResponseWriter<StringResponse, WebletWriteOptions>
        implements WebletTeleWriter<StringResponse> {


    @Inject
    public StringWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    public void write(StringResponse value, Class<StringResponse> valueType, WebletWriteOptions options) {

        HttpResponse response = httpResponse.get();

        if (value == null) {
            response.setStatus(204)
                    .setContentType(TextResponse.DEFAULT_CONTENT_TYPE)
                    .sendText("");
            return;
        }

        super.write(value, valueType, options);

        if (value.content() == null) {
            response.setStatus(204)
                    .setContentType(TextResponse.DEFAULT_CONTENT_TYPE)
                    .sendText("");
        } else {
            response.sendText(value.content());
        }
    }
}
