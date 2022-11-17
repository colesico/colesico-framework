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

import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.RpcMethod;
import colesico.framework.teleapi.TeleMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RpcLigature {
    public static final String ADD_API_METHOD = "addApi";
    public static final String API_METHOD = "api";

    /**
     * Supported RPC APIs (rpcNamespace -> api)
     */
    private final List<RpcApiSpec> rpcApiList = new ArrayList<>();

    public RpcLigature addApi(RpcApiSpec rpcApi) {
        rpcApiList.add(rpcApi);
        return this;
    }

    public List<RpcApiSpec> getRpcApiList() {
        return rpcApiList;
    }

    /**
     * Register new RPC API
     *
     * @param rpcName API RPC name. By default this is an RPC interface name
     */
    public static RpcApiSpec api(String rpcNamespace, String rpcName) {
        return new RpcApiSpec(rpcNamespace, rpcName);
    }

    public static RpcApiSpec api(String rpcName) {
        return new RpcApiSpec(RpcApi.DEFAULT_NAMESPACE, rpcName);
    }

    /**
     * RPC API ligature
     */
    public static final class RpcApiSpec {
        public static final String ADD_RPC_METHOD = "addRpcMethod";

        private final String rpcNamespace;

        /**
         * API rpc name.
         * An interface name annotated with {@link RpcMethod}
         */
        private final String rpcName;

        /**
         * Map RPC method name to tele-method.
         */
        private final Map<String, TeleMethod> rpcMethods = new HashMap<>();

        /**
         * Constructor
         *
         * @param rpcNamespace API rpcNamespace
         * @param rpcName   RPC interface name
         */
        public RpcApiSpec(String rpcNamespace, String rpcName) {
            this.rpcNamespace = rpcNamespace;
            this.rpcName = rpcName;
        }

        public String getRpcNamespace() {
            return rpcNamespace;
        }

        /**
         * Returns RPC interface name
         */
        public String getRpcName() {
            return rpcName;
        }

        public Map<String, TeleMethod> getRpcMethods() {
            return rpcMethods;
        }

        /**
         * Register RPC method.
         *
         * @param rpcName method RPC name. By default it is a APC API interface method name
         */
        public RpcApiSpec addRpcMethod(String rpcName, TeleMethod teleMethod) {
            TeleMethod prev = rpcMethods.put(rpcName, teleMethod);
            if (prev != null) {
                throw new RpcException("Method with name '" + rpcName + "' has already registered");
            }
            return this;
        }
    }

}
