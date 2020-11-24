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

import colesico.framework.rpc.RpcException;
import colesico.framework.teleapi.TeleMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RpcLigature {
    public static final String ADD_API_METHOD = "addApi";
    public static final String API_METHOD = "api";
    public static final String METHOD_METHOD = "method";

    /**
     * Supported RPC APIs
     */
    private final List<RpcApi> rpcApiList = new ArrayList<>();

    public RpcLigature addApi(RpcApi rpcApi) {
        rpcApiList.add(rpcApi);
        return this;
    }

    public List<RpcApi> getRpcApiList() {
        return rpcApiList;
    }

    /**
     * Register new RPC API
     * @param name RPC API name. By default this is an RPC API interface name
     * @return
     */
    public static RpcApi api(String name) {
        return new RpcApi(name);
    }

    public static RpcMethod method(TeleMethod teleMethod, Class<? extends RpcRequest> requestClass, Class<? extends RpcResponse> responseClass) {
        return new RpcMethod(teleMethod, requestClass, responseClass);
    }

    /**
     * RPC API ligature
     */
    public static final class RpcApi {
        public static final String ADD_METHOD = "addMethod";

        /**
         * RPC API name.
         * An interface name annotated with @RpcApi
         */
        private final String name;

        /**
         * RPC service methods metadata
         */
        private final Map<String, RpcMethod> targetMethods = new HashMap<>();

        /**
         * Constructor
         *
         * @param name RPC interface name
         */
        public RpcApi(String name) {
            this.name = name;
        }

        /**
         * Returns RPC interface name
         */
        public String getName() {
            return name;
        }

        public Map<String, RpcMethod> getTargetMethods() {
            return targetMethods;
        }

        /**
         * Register RPC method.
         * @param name RPC method name. By default it is a APC API interface method name
         * @param targetMethod
         * @return
         */
        public RpcApi addMethod(String name, RpcMethod targetMethod) {
            RpcMethod prev = targetMethods.put(name, targetMethod);
            if (prev != null) {
                throw new RpcException("Method with name '" + name + "' has already registered");
            }
            return this;
        }
    }

    /**
     * RPC Method ligature
     */
    public static final class RpcMethod {
        private final TeleMethod teleMethod;
        private final Class<? extends RpcRequest> requestType;
        private final Class<? extends RpcResponse> responseType;

        public RpcMethod(TeleMethod teleMethod, Class<? extends RpcRequest> requestType, Class<? extends RpcResponse> responseType) {
            this.teleMethod = teleMethod;
            this.requestType = requestType;
            this.responseType = responseType;
        }

        public Class<? extends RpcRequest> getRequestType() {
            return requestType;
        }

        public Class<? extends RpcResponse> getResponseType() {
            return responseType;
        }

        public TeleMethod getTeleMethod() {
            return teleMethod;
        }
    }
}
