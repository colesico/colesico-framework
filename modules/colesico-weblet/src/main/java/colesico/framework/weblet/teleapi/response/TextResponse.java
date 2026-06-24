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

package colesico.framework.weblet.teleapi.response;

import colesico.framework.telehttp.response.StringResponse;

import java.nio.charset.Charset;

/**
 * Simple text response
 */
public final class TextResponse extends StringResponse {

    public TextResponse(Integer statusCode, String contentType, String content, Charset charset) {
        super(statusCode, contentType, content, charset);
    }

    public static TextResponse of(String content) {
        return new TextResponse(
                200,
                null,
                content,
                null
        );
    }

    public static TextResponse of(String contentType, String content) {
        return new TextResponse(
                200,
                contentType,
                content,
                null
        );
    }

    public static TextResponse of(int statusCode, String content) {
        return new TextResponse(
                statusCode,
                null,
                content,
                null
        );
    }

    public static TextResponse of(int statusCode, String contentType, String content) {
        return new TextResponse(
                statusCode,
                contentType,
                content,
                null
        );
    }

}
