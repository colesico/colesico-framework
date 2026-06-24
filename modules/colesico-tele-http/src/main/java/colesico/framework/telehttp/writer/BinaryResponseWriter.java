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
import colesico.framework.telehttp.response.BinaryResponse;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class BinaryResponseWriter
        extends TeleHttpResponseWriter<BinaryResponse, HttpWriteOptions> {

    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    @Inject
    public BinaryResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected String contentType(BinaryResponse response, HttpWriteOptions options, String defaultValue) {
        return super.contentType(response, options, DEFAULT_CONTENT_TYPE);
    }

    @Override
    protected void writeResponse(HttpResponse protocol, BinaryResponse response, HttpWriteOptions options, Integer statusCode, String contentType) {

        // Force download?
        if (response.fileName() != null) {
            protocol.setHeader("Content-Disposition", "attachment; filename=\"" + response.fileName() + "\"");
        }

        if (response.content() == null || response.content().length == 0) {
            protocol.setStatus(204).sendData(ByteBuffer.allocate(0));
        } else {
            ByteBuffer buffer = ByteBuffer.wrap(response.content());
            protocol.sendData(buffer);
        }
    }
}
