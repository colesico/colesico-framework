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

    public static final String MIME_TYPE = "application/octet-stream";

    private static final TeleHttpResponseWriter.WriterOptions WRITER_OPTIONS =
            TeleHttpResponseWriter.WriterOptions.of(200, MediaType.ofCharset(MIME_TYPE, "utf-8"));

    @Inject
    public BytesResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse, WRITER_OPTIONS);
    }

    @Override
    protected void writeResponse(HttpResponse protocol,
                                 BytesResponse response,
                                 TeleHttpWriteOptions options,
                                 Integer statusCode,
                                 MediaType mediaType) {

        // Force download?
        if (response.fileName() != null) {
            protocol.setHeader("Content-Disposition", "attachment; filename=\"" + response.fileName() + "\"");
        }

        if (response.value() == null || response.value().length == 0) {
            protocol.setStatus(204).close();
        } else {
            protocol.send(response.value());
        }
    }
}
