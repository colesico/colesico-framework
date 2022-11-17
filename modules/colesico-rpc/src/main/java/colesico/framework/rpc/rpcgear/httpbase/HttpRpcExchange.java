package colesico.framework.rpc.rpcgear.httpbase;

import colesico.framework.assist.StrUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.http.HttpValues;
import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Http based exchange
 */
abstract public class HttpRpcExchange implements RpcExchange {

    protected final Logger logger = LoggerFactory.getLogger(RpcExchange.class);

    private final Provider<HttpContext> httpContextProv;

    public HttpRpcExchange(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    abstract protected <T> void serialize(T obj, OutputStream os);

    abstract protected <T> T deserialize(InputStream is, Class<T> type);

    @Override
    public Operation resolveOperation() {
        HttpValues<String, String> headers = httpContextProv.get().getRequest().getHeaders();
        String rpcNamespace = headers.get(HttpRpcClient.RPC_NAMESPACE_HEADER);

        if (StrUtils.isEmpty(rpcNamespace)){
            rpcNamespace = RpcApi.DEFAULT_NAMESPACE;
        }

        Operation resolution = new Operation(
                rpcNamespace,
                headers.get(HttpRpcClient.RPC_API_HEADER),
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
    public void sendError(RpcError error) {
        HttpResponse response = httpContextProv.get().getResponse();
        response.setContenType(HttpRpcClient.RPC_CONTENT_TYPE);
        String exceptionType = StringUtils.isBlank(error.getExceptionType()) ? "" : error.getExceptionType();
        String message = StringUtils.isBlank(error.getMessage()) ? "" : error.getMessage();
        response.setHeader(HttpRpcClient.RPC_ERROR_HEADER, exceptionType + ':' + message);
    }
}
