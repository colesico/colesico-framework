package colesico.framework.rpc.teleapi.client;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class RpcEndpointsPrototype {

    abstract public void addEndpoints(EndpointsRegistry registry);

    public interface EndpointsRegistry {
        void addEndpoint(String rpcApiName, String endpoint);

        void addEndpoint(Class<?> rpcApiClass, String endpoint);
    }
}
