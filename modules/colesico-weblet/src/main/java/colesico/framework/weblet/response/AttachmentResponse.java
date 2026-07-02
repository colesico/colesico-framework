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

import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.response.BytesResponse;
import colesico.framework.telehttp.response.ValueResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Simple data file response
 */
public final class AttachmentResponse extends BytesResponse {

    public AttachmentResponse(Integer statusCode, ContentType contentType, byte[] content, String fileName) {
        super(statusCode, contentType, content);
        if (fileName != null) {
            addHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodeFileName(fileName));
        }
    }

    private String encodeFileName(String fileName) {
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        // RFC 5987 -> %20
        return encoded.replace("+", "%20");
    }

}
