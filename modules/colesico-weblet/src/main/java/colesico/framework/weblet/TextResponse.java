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
 * Simple text response
 */
public final class TextResponse extends StringResponse {

    public static final String DEFAULT_CONTENT_TYPE = "text/plain; charset=utf-8";

    private TextResponse(String content, String contentType, int httpCode) {
        super(content, contentType, httpCode);
    }

    public static TextResponse of(String content, String contentType, int httpCode) {
        return new TextResponse(content, contentType, httpCode);
    }

    public static TextResponse of(String content, String contentType) {
        return new TextResponse(content, contentType, DEFAULT_HTTP_CODE);
    }

    public static TextResponse of(String content) {
        return new TextResponse(content, DEFAULT_CONTENT_TYPE, DEFAULT_HTTP_CODE);
    }

}
