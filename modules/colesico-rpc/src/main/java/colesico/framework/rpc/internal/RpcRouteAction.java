package colesico.framework.rpc.internal;

import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpValues;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.RpcDispatcher;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public final class RpcRouteAction {

    public static final String DISPATCHER_ROUTE = "/rpc";
    public static final String CLASS_NAME_PARAM = "class";
    public static final String METHOD_NAME_PARAM = "method";

    private final Provider<HttpRequest> requestProv;
    private final RpcDispatcher dispatcher;

    public RpcRouteAction(Provider<HttpRequest> requestProv, RpcDispatcher dispatcher) {
        this.requestProv = requestProv;
        this.dispatcher = dispatcher;
    }

    public void dispatch() {
        HttpRequest request = requestProv.get();
        HttpValues<String, String> params = request.getQueryParameters();
        String className = params.get(CLASS_NAME_PARAM);
        String methodName = params.get(METHOD_NAME_PARAM);
        dispatcher.dispatch(className, methodName);
    }

}
