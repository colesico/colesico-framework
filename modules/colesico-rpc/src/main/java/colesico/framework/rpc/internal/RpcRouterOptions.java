package colesico.framework.rpc.internal;

import colesico.framework.config.Config;
import colesico.framework.http.HttpMethod;
import colesico.framework.router.RouterBuilder;
import colesico.framework.router.RouterOptions;
import colesico.framework.rpc.teleapi.RpcExchange;

import javax.inject.Inject;

@Config
public class RpcRouterOptions extends RouterOptions {

    public static final String RPC_DISPATCHER_ROUTE = "/rpc";

    private final RpcDispatcher dispatcher;
    private final RpcExchange exchange;

    @Inject
    public RpcRouterOptions(RpcDispatcher dispatcher, RpcExchange exchange) {
        this.dispatcher = dispatcher;
        this.exchange = exchange;
    }

    @Override
    public void applyOptions(RouterBuilder builder) {
        builder.addCustomAction(HttpMethod.HTTP_METHOD_POST,
                RPC_DISPATCHER_ROUTE,
                RpcRouterOptions.class,
                () -> dispatcher.dispatch(exchange),
                "dispatch",
                null);
    }
}
