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

import colesico.framework.telehttp.response.ContentResponse;

/**
 * Binary data to  returned to  client
 */
public final class BinaryResponse extends ContentResponse<byte[]> {

    public static final Integer DEFAULT_STATUS_CODE = 200;
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final String fileName;

    public BinaryResponse(Integer statusCode, String contentType, byte[] content, String fileName) {
        super(statusCode, contentType, content);
        this.fileName = fileName;
    }

    public static BinaryResponse of(byte[] content) {
        return new BinaryResponse(
                DEFAULT_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                content,
                null);
    }

    public static BinaryResponse of(String contentType, byte[] content) {
        return new BinaryResponse(
                DEFAULT_STATUS_CODE,
                contentType,
                content,
                null);
    }

    public static BinaryResponse of(String contentType, byte[] content, String fileName) {
        return new BinaryResponse(
                DEFAULT_STATUS_CODE,
                contentType,
                content,
                fileName);
    }

    public static BinaryResponse of(int statusCode, String contentType, byte[] content, String fileName) {
        return new BinaryResponse(
                statusCode,
                contentType,
                content,
                fileName);
    }

    public String fileName() {
        return fileName;
    }

    public DynamicResponse wrap() {
        return DynamicResponse.of(this);
    }

}
