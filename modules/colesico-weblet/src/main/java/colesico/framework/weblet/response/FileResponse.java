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

import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.response.ValueResponse;

/**
 * Simple binary data actualResponse
 */
public final class FileResponse extends ValueResponse<byte[]> {

    private final String fileName;

    public FileResponse(Integer statusCode, MediaType mediaType, byte[] content, String fileName) {
        super(statusCode, mediaType, content);
        this.fileName = fileName;
    }

    public static FileResponse of(byte[] content) {
        return new FileResponse(
                null,
                null,
                content,
                null);
    }

    public static FileResponse of(MediaType mediaType, byte[] content) {
        return new FileResponse(
                null,
                mediaType,
                content,
                null);
    }

    public static FileResponse of(Integer statusCode, byte[] content) {
        return new FileResponse(
                statusCode,
                null,
                content,
                null);
    }

    public static FileResponse of(Integer statusCode, MediaType mediaType, byte[] content) {
        return new FileResponse(
                statusCode,
                mediaType,
                content,
                null);
    }

    public static FileResponse of(int statusCode, MediaType mediaType, byte[] content, String fileName) {
        return new FileResponse(
                statusCode,
                mediaType,
                content,
                fileName);
    }

    public String fileName() {
        return fileName;
    }

}
