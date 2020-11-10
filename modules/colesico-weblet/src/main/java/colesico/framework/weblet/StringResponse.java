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

package colesico.framework.weblet;

/**
 * String content based response
 */
abstract public class StringResponse {

    public static final int DEFAULT_HTTP_CODE = 200;

    /**
     * String content
     */
    protected final String content;

    /**
     * Response content type
     */
    protected final String contentType;

    /**
     * Http response code
     */
    protected final int httpCode;

    protected StringResponse(String content, String contentType, int httpCode) {
        this.content = content;
        this.contentType = contentType;
        this.httpCode = httpCode;
    }

    public WebletResponse wrap() {
        return WebletResponse.of(this);
    }
    
    public final String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
