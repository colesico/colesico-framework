package colesico.framework.rpc.clientapi;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Implement this config proto to define RPC endpoints for RPC client
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class RpcEndpointsPrototype {

    /**
     *  In the body of the method, add RPC endpoints to the registry
     */
    abstract public void addEndpoints(EndpointsRegistry registry);

    public interface EndpointsRegistry {
        void addEndpoint(String rpcApiName, String endpoint);

        /**
         * Define rpc target endpoint by remote RPC interface class
         * @param rpcApiClass - remote RPC interface class
         */
        void addEndpoint(Class<?> rpcApiClass, String endpoint);
    }
}
