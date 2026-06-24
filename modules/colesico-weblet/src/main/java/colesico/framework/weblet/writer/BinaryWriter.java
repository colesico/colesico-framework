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

package colesico.framework.weblet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import colesico.framework.weblet.response.BinaryResponse;
import colesico.framework.weblet.WebletTeleWriter;
import colesico.framework.weblet.WebletWriteOptions;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class BinaryWriter
        extends TeleHttpResponseWriter<BinaryResponse, WebletWriteOptions>
        implements WebletTeleWriter<BinaryResponse> {

    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    @Inject
    public BinaryWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected String contentType(BinaryResponse response, WebletWriteOptions options,String defaultValue) {
        return super.contentType(response,options,DEFAULT_CONTENT_TYPE);
    }

    @Override
    protected void writeResponse(HttpResponse protocol, BinaryResponse response, WebletWriteOptions options, Integer statusCode, String contentType) {

        // Force download?
        if (response.fileName() != null) {
            protocol.setHeader("Content-Disposition", "attachment; filename=\"" + response.fileName() + "\"");
        }

        if (response.content() == null || response.content().length == 0) {
            protocol.setStatus(204).sendText("");
        } else {
            ByteBuffer buffer = ByteBuffer.wrap(response.content());
            protocol.sendData(buffer);
        }
    }
}
