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
     * Supported RPC APIs (namespace -> api)
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
     *
     * @param rpcName API RPC name. By default this is an RPC interface name
     */
    public static RpcApi api(String namespace, String rpcName) {
        return new RpcApi(namespace, rpcName);
    }

    public static RpcApi api(String rpcName) {
        return new RpcApi(null, rpcName);
    }

    /**
     * RPC API ligature
     */
    public static final class RpcApi {
        public static final String ADD_METHOD = "addMethod";

        private final String namespace;

        /**
         * API rpc name.
         * An interface name annotated with {@link RpcMethod}
         */
        private final String rpcName;

        /**
         * Map RPC method name to tele-method.
         */
        private final Map<String, TeleMethod> teleMethods = new HashMap<>();

        /**
         * Constructor
         *
         * @param namespace API namespace
         * @param rpcName   RPC interface name
         */
        public RpcApi(String namespace, String rpcName) {
            this.namespace = namespace;
            this.rpcName = rpcName;
        }

        public String getNamespace() {
            return namespace;
        }

        /**
         * Returns RPC interface name
         */
        public String getRpcName() {
            return rpcName;
        }

        public Map<String, TeleMethod> getTeleMethods() {
            return teleMethods;
        }

        /**
         * Register RPC method.
         *
         * @param name RPC method name. By default it is a APC API interface method name
         */
        public RpcApi addMethod(String name, TeleMethod teleMethod) {
            TeleMethod prev = teleMethods.put(name, teleMethod);
            if (prev != null) {
                throw new RpcException("Method with name '" + name + "' has already registered");
            }
            return this;
        }
    }

}
