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

        @Override
        public String toString() {
            return "ROUTE";
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

        @Override
        public String toString() {
            return "QUERY";
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

        @Override
        public String toString() {
            return "POST";
        }
    };

    OriginFacade BODY = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return Origin.BODY;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            try (InputStream inputStream = httpRequest.getInputStream()) {
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                return result.toString(StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "BODY";
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

        @Override
        public String toString() {
            return "HEADER";
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

        @Override
        public String toString() {
            return "COOKIE";
        }
    };

    /**
     * This is AUTO default impl. Should be implemented for the concrete data port.
     */
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
                    return BODY.getString(name, routerContext, httpRequest);
                default:
                    return value;
            }
        }

        @Override
        public String toString() {
            return "AUTO";
        }
    };

    Origin getOrigin();

    String getString(String name, RouterContext routerContext, HttpRequest httpRequest);
}
