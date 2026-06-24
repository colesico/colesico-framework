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

package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.StringResponse;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * General {@link StringResponse} writer
 */
@Singleton
public class StringResponseWriter<V extends StringResponse, O extends HttpWriteOptions>
        extends TeleHttpResponseWriter<V, O> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Inject
    public StringResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    protected Charset defaultCharset(V value, O options) {
        return DEFAULT_CHARSET;
    }

    @Override
    protected void writeValue(HttpResponse response, V value, O options, Integer effectiveStatusCode, String effectiveContentType) {

        if (value.content() == null) {
            response.setStatus(204).sendText("");
            return;
        }

        var charset = value.charset();
        if (charset == null) {
            charset = options.charset();
            if (charset == null) {
                charset = defaultCharset(value, options);
            }
        }

        var content = value.content();
        response.sendData(ByteBuffer.wrap(content.getBytes(charset)));
    }

}
