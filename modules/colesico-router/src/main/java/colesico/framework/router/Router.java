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

package colesico.framework.router;

import colesico.framework.http.HttpMethod;
import colesico.framework.teleapi.TeleMethod;

import java.util.List;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public interface Router {

    /**
     * Returns route parts associated with given handler
     * @param targetClass service class or custom action class
     * @param targetMethodName service tele method name or custom action method name
     * @param httpMethod http request method
     * @param parameters route parameters
     * @return
     */
    List<String> getSlicedRoute(Class<?> targetClass, String targetMethodName, HttpMethod httpMethod, Map<String, String> parameters);

     /**
     * Calls handler associated with uri  (e.g. service method or custom handler)
     * @param httpMethod
     * @param uri
     */
    void dispatch(HttpMethod httpMethod, String uri);
}
