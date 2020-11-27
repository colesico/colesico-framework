package colesico.framework.rpc.teleapi.client;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class RpcClientEndpointsPrototype {

    abstract public void addEndpoints(RpcEndpoints rpcEndpoints);

    public interface RpcEndpoints {
        void addEndpoint(String rpcApiName, String endpoint);

        void addEndpoint(Class<?> rpcApiClass, String endpoint);
    }
}
