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

package colesico.framework.telehttp.reader;

import colesico.framework.http.HttpFile;
import colesico.framework.http.HttpRequest;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;

import javax.inject.Provider;
import javax.inject.Singleton;


/**
 * @author Vladlen Larionov
 */
@Singleton
public final class HttpFileReader<C extends HttpTRContext> implements HttpTeleReader<HttpFile, C> {

    private final Provider<HttpRequest> requestProv;

    public HttpFileReader(Provider<HttpRequest> requestProv) {
        this.requestProv = requestProv;
    }

    @Override
    public HttpFile read(C context) {
        return requestProv.get().getPostFiles().get(context.getParamName());
    }
}
