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

package colesico.framework.httpserver;

import colesico.framework.http.HttpContext;

/**
 * Global top-level exception handler.
 * <p>
 * Serves as the ultimate fallback mechanism for the application. It intercepts
 * any unhandled errors and exceptions thrown during HTTP request processing
 * to prevent abrupt server failure and ensure a graceful error response.
 * </p>
 *
 * @author Vladlen Larionov
 */
public interface ErrorHandler {

    /**
     * Handles the unhandled exception within the current HTTP context.
     *
     * @param throwable   the caught exception or error of any type
     * @param httpContext the current HTTP context used to format and send the response
     */
    void handleException(Throwable throwable, HttpContext httpContext);
}
