package colesico.framework.rpc.teleapi.http;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

abstract public class HttpRpcExchange implements RpcExchange {

    protected final Logger logger = LoggerFactory.getLogger(RpcExchange.class);

    private final Provider<HttpContext> httpContextProv;

    public HttpRpcExchange(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    abstract protected <T> void serialize(T obj, OutputStream os);

    abstract protected <T> T deserialize(InputStream is, Class<T> type);

    @Override
    public RequestResolution resolveRequest() {
        Map<String, String> headers = httpContextProv.get().getRequest().getHeaders();
        RequestResolution resolution = new RequestResolution(headers.get(HttpRpcClient.RPC_API_HEADER),
                headers.get(HttpRpcClient.RPC_METHOD_HEADER));
        return resolution;
    }

    @Override
    public <Q extends RpcRequest> Q readRequest(Type requestType) {
        HttpContext ctx = httpContextProv.get();
        Q request = deserialize(ctx.getRequest().getInputStream(), (Class<Q>) requestType);
        return request;
    }

    @Override
    public void writeResponse(RpcResponse response) {
        HttpContext ctx = httpContextProv.get();
        ctx.getResponse().setContenType(HttpRpcClient.RPC_CONTENT_TYPE);
        serialize(response, ctx.getResponse().getOutputStream());
    }

    @Override
    public void writeException(Throwable t) {
        Throwable cause = ExceptionUtils.getRootCause(t);
        if (cause == null) {
            cause = t;
        }
        RpcError error = RpcError.of(t.getClass().getCanonicalName(), cause.getMessage());

        HttpResponse response = httpContextProv.get().getResponse();
        response.setContenType(HttpRpcClient.RPC_CONTENT_TYPE);
        response.setHeader(HttpRpcClient.RPC_ERROR_HEADER, cause.getClass().getName());

        serialize(error, response.getOutputStream());
    }
}
