package colesico.framework.rpc.internal;

import colesico.framework.config.Config;
import colesico.framework.http.HttpMethod;
import colesico.framework.router.RouterBuilder;
import colesico.framework.router.RouterOptions;

import javax.inject.Inject;

@Config
public class RpcRouterOptions extends RouterOptions {

    private final RpcRouteAction actionHandler;

    @Inject
    public RpcRouterOptions(RpcRouteAction actionHandler) {
        this.actionHandler = actionHandler;
    }

    @Override
    public void applyOptions(RouterBuilder builder) {
        builder.addRouteAction(HttpMethod.HTTP_METHOD_POST,
                RpcRouteAction.DISPATCHER_ROUTE,
                RpcRouteAction.class,
                actionHandler::dispatch,
                "dispatch");

    }
}
