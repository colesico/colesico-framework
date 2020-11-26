package colesico.framework.rpc.kryo;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.http.HttpRpcClient;
import colesico.framework.rpc.http.HttpRpcExchange;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

@Singleton
public class KryoExchange extends HttpRpcExchange {

    private final KryoSerializer serializer;

    public KryoExchange(Provider<HttpContext> httpContextProv, KryoSerializer serializer) {
        super(httpContextProv);
        this.serializer = serializer;
    }

    @Override
    protected <T> void serialize(T obj, OutputStream os) {
        serializer.serialize(obj, os);
    }

    @Override
    protected <T> T deserialize(InputStream is, Class<T> type) {
        return serializer.deserialize(is, type);
    }
}
