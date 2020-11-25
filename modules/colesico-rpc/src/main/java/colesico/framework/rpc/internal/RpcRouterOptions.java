package colesico.framework.rpc.internal;

import colesico.framework.config.Config;
import colesico.framework.http.HttpMethod;
import colesico.framework.router.RouterBuilder;
import colesico.framework.router.RouterOptions;
import colesico.framework.rpc.teleapi.RpcExchange;

import javax.inject.Inject;

@Config
public class RpcRouterOptions extends RouterOptions {

    public static final String RPC_CONTROLLER_ROUTE = "/rpc";

    private final RpcController controller;
    private final RpcExchange exchange;

    @Inject
    public RpcRouterOptions(RpcController controller, RpcExchange exchange) {
        this.controller = controller;
        this.exchange = exchange;
    }

    @Override
    public void applyOptions(RouterBuilder builder) {
        builder.addCustomAction(HttpMethod.HTTP_METHOD_POST,
                RPC_CONTROLLER_ROUTE,
                RpcRouterOptions.class,
                () -> controller.dispatch(exchange),
                "dispatch",
                null);
    }
}
