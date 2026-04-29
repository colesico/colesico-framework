/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.router;

import colesico.framework.http.HttpMethod;
import colesico.framework.teleapi.TeleController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Router API
 * <p>
 * Router allows to bind any action to given route and perform it
 */
public interface Router extends TeleController<Router.Criteria, Router.Invocation, RouterCommands> {

    /**
     * Returns route parts associated with given handler
     *
     * @param targetClass  service class or custom action class
     * @param targetMethod service  method name or custom action method name
     * @param httpMethod   http request method
     * @param parameters   route parameters
     */
    List<String> slicedRoute(Class<?> targetClass, String targetMethod, HttpMethod httpMethod, Map<String, String> parameters);

    default Optional<Invocation> resolve(HttpMethod requestMethod, String requestUri) {
        return resolve(Router.Criteria.of(requestMethod, requestUri));
    }

    /**
     * Route invocation resolving criteria
     *
     * @param requestUri request url part from hostname(port) to query string (before '?' char)
     */
    record Criteria(HttpMethod requestMethod, String requestUri) implements TeleController.Criteria {
        public static Criteria of(HttpMethod requestMethod, String requestUri) {
            return new Criteria(requestMethod, requestUri);
        }
    }

    /**
     * @param requestMethod Request http method
     * @param requestUri    Request URI
     * @param action        Route Action
     * @param parameters    Route parameters
     */
    record Invocation(HttpMethod requestMethod,
                      String requestUri,
                      RouteAction action,
                      Map<String, String> parameters) implements TeleController.Invocation {
    }

}
