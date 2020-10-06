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
 * Represents text/binary or navigation response
 *
 * @author Vladlen Larionov
 */
public final class VariedResponse {
    private final NavigationResponse navigationResponse;
    private final StringResponse stringResponse;
    private final BinaryResponse binaryResponse;

    private VariedResponse(NavigationResponse navigationResponse, StringResponse stringResponse, BinaryResponse binaryResponse) {
        this.navigationResponse = navigationResponse;
        this.stringResponse = stringResponse;
        this.binaryResponse = binaryResponse;
    }

    public static VariedResponse of(NavigationResponse navigationResponse) {
        return new VariedResponse(navigationResponse, null, null);
    }

    public static VariedResponse of(StringResponse stringResponse) {
        return new VariedResponse(null, stringResponse, null);
    }

    public static VariedResponse of(BinaryResponse binaryResponse) {
        return new VariedResponse(null, null, binaryResponse);
    }

    public static VariedResponse nav(Class<?> serviceClass, String targetMethodName) {
        return new VariedResponse(NavigationResponse.of(serviceClass, targetMethodName), null, null);
    }

    public static VariedResponse nav(String uri) {
        return new VariedResponse(NavigationResponse.of(uri), null, null);
    }

    public static VariedResponse bin(byte[] content, String mimeType, String fileName) {
        return new VariedResponse(null, null, BinaryResponse.of(content, mimeType, fileName));
    }

    public static VariedResponse bin(byte[] content, String mimeType) {
        return new VariedResponse(null, null, BinaryResponse.of(content, mimeType));
    }

    public static VariedResponse bin(byte[] content) {
        return new VariedResponse(null, null, BinaryResponse.of(content));
    }

    public static VariedResponse text(String content) {
        return new VariedResponse(null, TextResponse.of(content), null);
    }

    public static VariedResponse html(String content) {
        return new VariedResponse(null, HtmlResponse.of(content), null);
    }

    public NavigationResponse getNavigationResponse() {
        return navigationResponse;
    }

    public StringResponse getStringResponse() {
        return stringResponse;
    }

    public BinaryResponse getBinaryResponse() {
        return binaryResponse;
    }
}
