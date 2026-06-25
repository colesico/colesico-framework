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

package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

/**
 * String data response
 */
public class StringResponse extends ContentResponse<String> {

    public StringResponse(Integer statusCode, MediaType mediaType, String content) {
        super(statusCode, mediaType, content);
    }

    public static StringResponse of(String content) {
        return new StringResponse(null, null, content);
    }

    public static StringResponse of(MediaType mediaType, String content) {
        return new StringResponse(null, mediaType, content);
    }

    public static StringResponse of(Integer statusCode, String content) {
        return new StringResponse(statusCode, null, content);
    }

    public static StringResponse of(Integer statusCode, MediaType mediaType, String content) {
        return new StringResponse(statusCode, mediaType, content);
    }
}

