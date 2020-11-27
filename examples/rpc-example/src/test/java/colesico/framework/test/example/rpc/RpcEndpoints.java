package colesico.framework.test.example.rpc;

import colesico.framework.config.Config;
import colesico.framework.example.rpc.api.HelloServiceRemote;
import colesico.framework.rpc.teleapi.client.RpcEndpointsPrototype;

@Config
public class RpcEndpoints extends RpcEndpointsPrototype {
    @Override
    public void addEndpoints(EndpointsRegistry endpointsRegistry) {
        endpointsRegistry.addEndpoint(HelloServiceRemote.class,"http://localhost:8080/rpc");
    }
}
