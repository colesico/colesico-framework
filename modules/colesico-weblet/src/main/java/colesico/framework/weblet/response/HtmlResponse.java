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

package colesico.framework.weblet.response;

import colesico.framework.telehttp.response.StringResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * HTML text to  returned to  client
 */
public final class HtmlResponse extends StringResponse {

    public static final Integer DEFAULT_STATUS_CODE = 200;
    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public HtmlResponse(Integer statusCode, String contentType, String content, Charset charset) {
        super(statusCode, contentType, content, charset);
    }

    /**
     * Empty response
     */
    public static HtmlResponse of() {
        return new HtmlResponse(
                204,
                DEFAULT_CONTENT_TYPE,
                "",
                DEFAULT_CHARSET
        );
    }

    public static HtmlResponse of(String content) {
        return new HtmlResponse(
                DEFAULT_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                content,
                DEFAULT_CHARSET
        );
    }

    public static HtmlResponse of(int statusCode, String content) {
        return new HtmlResponse(
                statusCode,
                DEFAULT_CONTENT_TYPE,
                content,
                DEFAULT_CHARSET
        );
    }
}
