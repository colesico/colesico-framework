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
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import colesico.framework.weblet.response.BinaryResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;

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

    @Inject
    public BinaryWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    public void write(BinaryResponse value, Class<BinaryResponse> valueType, WebletWriteOptions options) {

        HttpResponse response = httpResponse.get();

        if (value == null) {
            response.setStatusCode(204)
                    .setContentType(BinaryResponse.DEFAULT_CONTENT_TYPE)
                    .sendData(ByteBuffer.allocate(0));
            return;
        }

        super.write(value, valueType, options);

        // Force download?
        if (value.fileName() != null) {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + value.fileName() + "\"");
        }

        if (value.content() == null || value.content().length == 0) {
            response.setStatusCode(204)
                    .sendData(ByteBuffer.allocate(0));
        } else {
            ByteBuffer buffer = ByteBuffer.wrap(value.content());
            response.sendData(buffer);
        }
    }

}
