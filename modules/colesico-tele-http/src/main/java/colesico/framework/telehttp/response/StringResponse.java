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

import java.nio.charset.Charset;

/**
 * String based content response
 */
abstract public class StringResponse extends ContentResponse<String> {

    private final Charset charset;

    public StringResponse(Integer statusCode, String contentType, String content, Charset charset) {
        super(statusCode, contentType, content);
        this.charset = charset;
    }

    public Charset charset() {
        return charset;
    }

}
