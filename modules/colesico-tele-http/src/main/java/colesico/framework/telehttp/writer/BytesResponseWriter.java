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
import colesico.framework.telehttp.response.BytesResponse;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class BytesResponseWriter
        extends TeleHttpResponseWriter<BytesResponse, TeleHttpWriteOptions> {

    @Inject
    public BytesResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected MediaType defaultMediaType() {
        return MediaType.ofCharset("application/octet-stream", "utf-8");
    }

    @Override
    protected void writeResponse(HttpResponse protocol,
                                 BytesResponse response,
                                 TeleHttpWriteOptions options,
                                 Integer statusCode,
                                 MediaType mediaType) {

        if (response.value() == null || response.value().length == 0) {
            protocol.setStatus(emptyStatusCode()).close();
            return;
        }

        // Force download?
        if (response.fileName() != null) {
            protocol.setHeader("Content-Disposition", "attachment; filename=\"" + response.fileName() + "\"");
        }

        protocol.setStatus(statusCode)
                .setContentType(toContentType(mediaType))
                .send(response.value());

    }
}
