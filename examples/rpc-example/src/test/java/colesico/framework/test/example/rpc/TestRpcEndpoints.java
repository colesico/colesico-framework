package colesico.framework.test.example.rpc;

import colesico.framework.config.Config;
import colesico.framework.example.rpc.api.HelloServiceRemote;
import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.rpc.clientapi.RpcEndpointsPrototype;

@Config
@Requires(TestCondition.class)
@Substitute
public class TestRpcEndpoints extends RpcEndpointsPrototype {
    @Override
    public void addEndpoints(EndpointsRegistry registry) {
        registry.addEndpoint(HelloServiceRemote.class,"http://localhost:8085/rpc");
    }
}
