package colesico.framework.rpc.internal;

import colesico.framework.config.Config;
import colesico.framework.http.HttpMethod;
import colesico.framework.router.RouterBuilder;
import colesico.framework.router.RouterOptions;
import colesico.framework.rpc.teleapi.RpcDispatcher;

import javax.inject.Inject;

@Config
public class RpcRouterOptions extends RouterOptions {

    private final RpcHttpDispatcher dispatcher;

    @Inject
    public RpcRouterOptions(RpcHttpDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void applyOptions(RouterBuilder builder) {
        builder.addRouteAction(HttpMethod.HTTP_METHOD_POST,
                RpcHttpDispatcher.DISPATCHER_ROUTE,
                RpcDispatcher.class,
                dispatcher::dispatch,
                "dispatch");

    }
}
