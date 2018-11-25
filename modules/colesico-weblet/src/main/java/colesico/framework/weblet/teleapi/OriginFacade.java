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

package colesico.framework.weblet.teleapi;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;

/**
 * @author Vladlen Larionov
 */
@FunctionalInterface
public interface OriginFacade {

    OriginFacade ROUTE = (name, r, h) -> r.getParameters().get(name);
    OriginFacade QUERY = (name, r, h) -> h.getQueryParameters().get(name);
    OriginFacade POST = (name, r, h) -> h.getPostParameters().get(name);
    OriginFacade HEADER = (name, r, h) -> h.getHeaders().get(name);
    OriginFacade COOKIE = (name, r, h) -> {
        HttpCookie cookie = h.getCookies().get(name);
        return cookie == null ? null : cookie.getValue();
    };

    OriginFacade DEFAULT = (name, r, h) -> {
        String value = r.getParameters().get(name);
        if (value != null) {
            return value;
        }
        value = h.getQueryParameters().get(name);
        if (value != null) {
            return value;
        }
        value = h.getPostParameters().get(name);
        return value;
    };

    String getString(String name, RouterContext routerContext, HttpRequest httpRequest);
}
