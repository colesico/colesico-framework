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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RpcLigature {
    public static final String ADD_API_METHOD = "addApi";

    /**
     * Supported RPC APIs
     */
    private final List<RpcApi> rpcApiList = new ArrayList<>();

    public RpcLigature addApi(RpcApi rpcApi) {
        rpcApiList.add(rpcApi);
        return this;
    }

    public List<RpcApi> getAllRpcApi() {
        return rpcApiList;
    }

    public static final class RpcApi {
        public static final String ADD_METHOD = "addMethod";
        /**
         * RPC interface name
         */
        private final String rpcInterface;

        /**
         * RPC service methods metadata
         */
        private final Map<String, RpcMethod> targetMethods = new HashMap<>();

        /**
         * Constructor
         *
         * @param rpcInterface RPC interface name
         */
        public RpcApi(String rpcInterface) {
            this.rpcInterface = rpcInterface;
        }

        /**
         * Returns RPC interface name
         */
        public String getRpcInterface() {
            return rpcInterface;
        }

        public Map<String, RpcMethod> getTargetMethods() {
            return targetMethods;
        }

        public RpcApi addMethod(String name, RpcMethod targetMethod) {
            RpcMethod prev = targetMethods.put(name, targetMethod);
            if (prev != null) {
                throw new RpcException("Method with name '" + name + "' has already registered");
            }
            return this;
        }
    }

    public static final class RpcMethod {
        private final TeleMethod teleMethod;
        private final Class<? extends RpcRequest> requestClass;
        private final Class<? extends RpcResponse> responseClass;

        public RpcMethod(TeleMethod teleMethod, Class<? extends RpcRequest> requestClass, Class<? extends RpcResponse> responseClass) {
            this.teleMethod = teleMethod;
            this.requestClass = requestClass;
            this.responseClass = responseClass;
        }

        public Class<? extends RpcRequest> getRequestClass() {
            return requestClass;
        }

        public Class<? extends RpcResponse> getResponseClass() {
            return responseClass;
        }

        public TeleMethod getTeleMethod() {
            return teleMethod;
        }
    }
}
