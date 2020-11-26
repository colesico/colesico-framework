package colesico.framework.rpc.kryo;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;
import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

@Singleton
public class KryoExchange implements RpcExchange {

    String RESPONSE_CONTENT_TYPE = "application/octet-stream";

    private final Provider<HttpContext> httpContextProv;
    private final KryoSerializer serializer;

    public KryoExchange(Provider<HttpContext> httpContextProv, KryoSerializer serializer) {
        this.httpContextProv = httpContextProv;
        this.serializer = serializer;
    }

    @Override
    public RequestResolution resolveRequest() {
        Map<String, String> headers = httpContextProv.get().getRequest().getHeaders();
        RequestResolution resolution = new RequestResolution(headers.get(KryoClient.RPC_API_HEADER),
                headers.get(KryoClient.RPC_METHOD_HEADER));
        return resolution;
    }

    @Override
    public <Q extends RpcRequest> Q readRequest(Type requestType) {
        HttpContext ctx = httpContextProv.get();
        Q request = serializer.deserialize(ctx.getRequest().getInputStream(), (Class<Q>) requestType);
        return request;
    }

    @Override
    public void writeResponse(RpcResponse response) {
        HttpContext ctx = httpContextProv.get();
        ctx.getResponse().setContenType(RESPONSE_CONTENT_TYPE);
        serializer.serialize(response, ctx.getResponse().getOutputStream());
    }

    @Override
    public void writeException(Throwable t) {
        Throwable cause = ExceptionUtils.getRootCause(t);
        if (cause == null) {
            cause = t;
        }
        RpcError error = RpcError.of(t.getClass().getCanonicalName(), cause.getMessage());

        HttpResponse response = httpContextProv.get().getResponse();
        response.setContenType(RESPONSE_CONTENT_TYPE);
        response.setHeader(KryoClient.RPC_ERROR_HEADER, cause.getClass().getName());

        serializer.serialize(error, response.getOutputStream());
    }

}
