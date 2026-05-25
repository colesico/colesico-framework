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
public final class StringWriter implements WebletTeleWriter<StringResponse> {

    private final Provider<HttpResponse> httpResponse;

    @Inject
    public StringWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(StringResponse value, Class<StringResponse> valueType, WebletWriteOptions options) {

        HttpResponse response = httpResponse.get();

        if (value == null) {
            response.sendText("", TextResponse.DEFAULT_CONTENT_TYPE, 204);
            return;
        }

        HttpUtils.setHeaders(response, value.headers());
        HttpUtils.setCookies(response, value.cookies());

        if (value.content() == null) {
            response.sendText("", TextResponse.DEFAULT_CONTENT_TYPE, 204);
        } else {
            response.sendText(value.content(), value.contentType(), value.statusCode());
        }
    }
}
