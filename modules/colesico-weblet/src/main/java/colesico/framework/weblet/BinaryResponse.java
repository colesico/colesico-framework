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
 * Binary data to returned to client
 */
public final class BinaryResponse {

    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final int DEFAULT_HTTP_CODE = 200;

    protected final byte[] content;

    protected final String contentType;

    protected final String fileName;

    /**
     * Http response code
     */
    protected final int httpCode;

    private BinaryResponse(byte[] content, String contentType, String fileName, int httpCode) {
        this.content = content;
        this.contentType = contentType;
        this.fileName = fileName;
        this.httpCode = httpCode;
    }

    public static BinaryResponse of(byte[] content, String contentType, String fileName, int httpCode) {
        return new BinaryResponse(content, contentType, fileName, httpCode);
    }

    public static BinaryResponse of(byte[] content, String contentType, String fileName) {
        return new BinaryResponse(content, contentType, fileName, DEFAULT_HTTP_CODE);
    }

    public static BinaryResponse of(byte[] content, String contentType) {
        return new BinaryResponse(content, contentType, null, DEFAULT_HTTP_CODE);
    }

    public static BinaryResponse of(byte[] content) {
        return new BinaryResponse(content, DEFAULT_CONTENT_TYPE, null, DEFAULT_HTTP_CODE);
    }

    public byte[] getContent() {
        return content;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
