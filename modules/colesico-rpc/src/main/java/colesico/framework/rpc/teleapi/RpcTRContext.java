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

package colesico.framework.rpc.teleapi;

/**
 * RPC tele-data reading context
 */
public final class RpcTRContext<R extends RpcRequest, V> {

    public static final String OF_METHOD = "of";

    private R request;
    private final ValueGetter<R, V> valueGetter;

    private RpcTRContext(ValueGetter<R, V> valueGetter) {
        this.valueGetter = valueGetter;
    }

    public R getRequest() {
        return request;
    }

    public void setRequest(R request) {
        this.request = request;
    }

    public ValueGetter<R, V> getValueGetter() {
        return valueGetter;
    }

    public static <R extends RpcRequest, V> RpcTRContext<R, V> of(ValueGetter<R, V> reader) {
        return new RpcTRContext<>(reader);
    }

    @FunctionalInterface
    public interface ValueGetter<R extends RpcRequest, V> {
        V get(R request);
    }
}
