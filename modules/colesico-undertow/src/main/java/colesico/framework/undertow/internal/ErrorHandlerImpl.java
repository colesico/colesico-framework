/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.undertow.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.undertow.AbstractErrorHandler;
import colesico.framework.undertow.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ErrorHandlerImpl extends AbstractErrorHandler {

    private Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public ErrorHandlerImpl(Provider<HttpContext> contextProv) {
        super(contextProv);
    }
}
