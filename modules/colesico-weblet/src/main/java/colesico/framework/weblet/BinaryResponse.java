/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.weblet;

/**
* @author Vladlen Larionov
*/
public final class BinaryResponse {

    protected final String fileName;
    protected  final String mimeType;
    protected  final byte[] content;

    public BinaryResponse(String contentType, byte[] bytes) {
        this.mimeType = contentType;
        this.content = bytes;
        this.fileName = null;
    }

    public BinaryResponse(byte[] bytes) {
        this.content = bytes;
        this.mimeType ="application/octet-stream";
        this.fileName = null;
    }

    public String getMimeType() {
        return mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public String getFileName() {
        return fileName;
    }
}
