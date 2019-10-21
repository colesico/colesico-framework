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
public final class TextResponse implements StringResponse {

    private final String content;
    private final String contentType;

    private TextResponse(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }
    
    public static TextResponse of (String content, String contentType){
        return new TextResponse(content,contentType);
    }

    public static TextResponse of (String content){
        return new TextResponse(content,"text/plain");
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getContent() {
        return content;
    }
}
