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

package colesico.framework.weblet.teleapi;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.Origin;

import java.io.InputStream;

/**
 * Facade to
 */
public interface OriginFacade {

    OriginFacade ROUTE = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.ROUTE;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return routerContext.getParameters().get(name);
        }
    };


    OriginFacade QUERY = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.QUERY;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return httpRequest.getQueryParameters().get(name);
        }
    };

    OriginFacade POST = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.POST;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return httpRequest.getPostParameters().get(name);
        }
    };

    OriginFacade BODY = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.BODY;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Use request input stream to read from this origin");
        }
    };

    OriginFacade HEADER = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.HEADER;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return httpRequest.getHeaders().get(name);
        }
    };

    OriginFacade COOKIE = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.COOKIE;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            HttpCookie cookie = httpRequest.getCookies().get(name);
            return cookie == null ? null : cookie.getValue();
        }
    };

    OriginFacade AUTO = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.AUTO;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            String value = null;
            switch (httpRequest.getRequestMethod().getName()) {
                case HttpMethod.GET:
                case HttpMethod.HEAD:
                    value = httpRequest.getQueryParameters().get(name);
                    if (value != null) {
                        return value;
                    }
                    return routerContext.getParameters().get(name);
                case HttpMethod.POST:
                case HttpMethod.PUT:
                case HttpMethod.PATCH:
                    value = httpRequest.getPostParameters().get(name);
                    if (value != null) {
                        return value;
                    }
                    value = httpRequest.getQueryParameters().get(name);
                    if (value != null) {
                        return value;
                    }
                    return routerContext.getParameters().get(name);
                default:
                    return value;
            }
        }
    };

    Origin getOrigin();

    String getString(String name, RouterContext routerContext, HttpRequest httpRequest);
}
