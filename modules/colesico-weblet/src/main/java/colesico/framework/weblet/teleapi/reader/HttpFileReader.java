/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.weblet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpFile;
import colesico.framework.http.HttpRequest;
import colesico.framework.weblet.teleapi.ReaderContext;
import colesico.framework.weblet.teleapi.WebletTeleReader;

import javax.inject.Provider;

/**
 * @author Vladlen Larionov
 */
public final class HttpFileReader implements WebletTeleReader<HttpFile> {

    protected final Provider<HttpContext> httpContextProv;

    public HttpFileReader(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public HttpFile read(ReaderContext ctx) {
        HttpRequest request = httpContextProv.get().getRequest();
        return request.getPostFiles().get(ctx.getName());
    }
}
