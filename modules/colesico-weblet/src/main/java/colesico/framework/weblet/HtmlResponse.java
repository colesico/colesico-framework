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
 * Html text to returned to client
 */
public final class HtmlResponse extends StringResponse {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";

    private HtmlResponse(String content, String contentType, int httpCode) {
        super(content, contentType, httpCode);
    }

    public static HtmlResponse of(String content) {
        return new HtmlResponse(content, DEFAULT_CONTENT_TYPE, DEFAULT_HTTP_CODE);
    }

    public static HtmlResponse of(String content, int httpCOde) {
        return new HtmlResponse(content, DEFAULT_CONTENT_TYPE, httpCOde);
    }
}
