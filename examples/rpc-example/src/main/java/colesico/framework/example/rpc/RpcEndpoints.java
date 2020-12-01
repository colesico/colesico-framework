package colesico.framework.example.rpc;

import colesico.framework.config.Config;
import colesico.framework.example.rpc.api.HelloServiceRemote;
import colesico.framework.rpc.clientapi.RpcEndpointsPrototype;

@Config
public class RpcEndpoints extends RpcEndpointsPrototype {
    @Override
    public void addEndpoints(EndpointsRegistry registry) {
        registry.addEndpoint(HelloServiceRemote.class,"http://localhost:8080/rpc");
    }
}
