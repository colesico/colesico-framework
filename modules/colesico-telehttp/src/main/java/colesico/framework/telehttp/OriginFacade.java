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

package colesico.framework.telehttp;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Default value reading facades for different origins
 */
abstract public class OriginFacade {

    public static final OriginFacade ROUTE = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.ORIGIN_ROUTE;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return routerContext.getParameters().get(name);
        }

    };

    public static final OriginFacade QUERY = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.ORIGIN_QUERY;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return httpRequest.getQueryParameters().get(name);
        }
    };

    public static final OriginFacade POST = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.ORIGIN_POST;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return httpRequest.getPostParameters().get(name);
        }
    };

    public static final OriginFacade BODY = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.ORIGIN_BODY;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Obtaining named parameter '" + name + "' from body is not supported");
        }
    };

    public static final OriginFacade HEADER = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.ORIGIN_HEADER;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return httpRequest.getHeaders().get(name);
        }
    };

    public static final OriginFacade COOKIE = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.ORIGIN_COOKIE;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            HttpCookie cookie = httpRequest.getCookies().get(name);
            return cookie == null ? null : cookie.getValue();
        }
    };

    /**
     * This is AUTO default impl. Should be implemented for the concrete data port.
     */
    public static final OriginFacade AUTO = new OriginFacade() {

        @Override
        public Origin getOrigin() {
            return Origin.ORIGIN_AUTO;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            String value = null;
            switch (httpRequest.getRequestMethod().getName()) {
                case HttpMethod.GET:
                case HttpMethod.HEAD:
                    if (httpRequest.getQueryParameters().hasKey(name)) {
                        return httpRequest.getQueryParameters().get(name);
                    }
                    return routerContext.getParameters().get(name);
                case HttpMethod.POST:
                case HttpMethod.PUT:
                case HttpMethod.PATCH:
                    if (httpRequest.getPostParameters().hasKey(name)) {
                        return httpRequest.getPostParameters().get(name);
                    }
                    if (httpRequest.getQueryParameters().hasKey(name)) {
                        return httpRequest.getQueryParameters().get(name);
                    }
                    if (routerContext.getParameters().containsKey(name)) {
                        return routerContext.getParameters().get(name);
                    }
                default:
                    return value;
            }
        }
    };

    abstract public Origin getOrigin();

    abstract public String getString(String name, RouterContext routerContext, HttpRequest httpRequest);

    @Override
    public String toString() {
        return getOrigin().getName();
    }

}
