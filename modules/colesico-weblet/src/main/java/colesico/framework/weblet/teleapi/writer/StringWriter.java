/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.weblet.StringResponse;
import colesico.framework.weblet.TextResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletTWContext;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class StringWriter extends WebletTeleWriter<StringResponse> {

    @Inject
    public StringWriter(Provider<HttpContext> httpContextProv) {
        super(httpContextProv);
    }

    @Override
    public void write(StringResponse value, WebletTWContext ctx) {
        if (value == null || value.getContent() == null) {
            getResponse().sendText("", TextResponse.DEFAULT_CONTENT_TYPE, 204);
        }
        getResponse().sendText(value.getContent(), value.getContentType(), value.getStatusCode());
    }
}
