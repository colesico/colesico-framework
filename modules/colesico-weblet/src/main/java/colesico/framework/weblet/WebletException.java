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

package colesico.framework.weblet;

/**
 * @author Vladlen Larionov
 */
public class WebletException extends RuntimeException {

    /**
     * Http response status code
     */
    private final Integer statusCode;

    /**
     * Returned data
     */
    private final Object details;

    public WebletException(String message, Throwable cause, Integer statusCode, Object details) {
        super(message, cause);
        this.statusCode = statusCode != null ? statusCode : 500;
        this.details = details;
    }

    public static WebletException of(Integer statusCode, Object details) {
        return new WebletException(String.valueOf(details), null, statusCode, details);
    }

    public static WebletException of(String message, Integer statusCode, Object details) {
        return new WebletException(message, null, statusCode, details);
    }

    public static WebletException of(Throwable cause, Integer statusCode, Object details) {
        return new WebletException(String.valueOf(details), cause, statusCode, details);
    }

    public Object details() {
        return details;
    }

    public Integer statusCode() {
        return statusCode;
    }
}
