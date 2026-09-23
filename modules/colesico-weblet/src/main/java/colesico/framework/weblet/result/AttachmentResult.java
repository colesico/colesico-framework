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

package colesico.framework.weblet.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.result.BytesResult;
import colesico.framework.telehttp.result.ValueResult;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple data file response
 */
public final class AttachmentResult extends BytesResult {

    public static final String CONTENT_DISPOSITION_HEADER = "content-disposition";

    private AttachmentResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, byte[] value) {
        super(status, contentType, headers, cookies, value);
    }

    public static AttachmentResult.Builder attachment(byte[] value, String fileName) {
        return new AttachmentResult.Builder(value, fileName);
    }

    public static class Builder extends ValueResult.Builder<byte[], AttachmentResult, AttachmentResult.Builder> {

        private final String fileName;

        public Builder(byte[] value, String fileName) {
            super(value);
            this.fileName = fileName;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public AttachmentResult build() {
            if (fileName != null) {
                String headerValue = "attachment; filename*=UTF-8''" + encodeFileName(fileName);
                header(CONTENT_DISPOSITION_HEADER, headerValue);
            }
            return new AttachmentResult(status, contentType, headers, cookies, value);
        }

        private String encodeFileName(String fileName) {
            String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            //  RFC 5987 ("+" -> %20)
            return encoded.replace("+", "%20");
        }
    }

}
