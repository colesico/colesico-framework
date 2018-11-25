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
public final class VariousResponse {
    private final NavigationResponse navigationResponse;
    private final StringResponse stringResponse;
    private final BinaryResponse binaryResponse;

    public VariousResponse(NavigationResponse navigationResponse) {
        this.navigationResponse = navigationResponse;
        this.stringResponse = null;
        this.binaryResponse = null;
    }

    public VariousResponse(StringResponse stringResponse) {
        this.stringResponse = stringResponse;
        this.binaryResponse = null;
        this.navigationResponse = null;
    }

    public VariousResponse(BinaryResponse binaryResponse) {
        this.binaryResponse = binaryResponse;
        this.stringResponse = null;
        this.navigationResponse = null;
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
