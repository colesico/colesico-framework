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
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
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
public class StringResponseWriter<R extends StringResponse, O extends TeleHttpWriteOptions>
        extends TeleHttpResponseWriter<R, O> {

    public static final MediaType DEFAULT_MEDIA_TYPE = MediaType.ofCharset(MediaType.TEXT_PLAIN, "utf-8");

    @Inject
    public StringResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected MediaType mediaType(R response, O options, MediaType defaultValue) {
        return super.mediaType(response, options, DEFAULT_MEDIA_TYPE);
    }



    @Override
    protected void writeResponse(HttpResponse protocol,
                                 R response,
                                 O options,
                                 Integer statusCode,
                                 MediaType mediaType) {



        var charset = charset(mediaType);
        protocol.sendData(ByteBuffer.wrap(response.value().getBytes(charset)));
    }

}
