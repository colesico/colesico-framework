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

package colesico.framework.telehttp.reader;

import colesico.framework.http.HttpFile;
import colesico.framework.http.HttpRequest;
import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.HttpTeleReader;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;


/**
 * @author Vladlen Larionov
 */
@Singleton
public final class HttpFileReader implements HttpTeleReader<HttpFile, HttpReadOptions> {

    private final Provider<HttpRequest> httpRequest;

    public HttpFileReader(Provider<HttpRequest> httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public HttpFile read(Class<HttpFile> valueType, HttpReadOptions options) {
        return httpRequest.get().postFiles().get(options.paramName());
    }
}
