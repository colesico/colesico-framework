package colesico.framework.rpc.internal.kryo;

import colesico.framework.http.HttpContext;
import colesico.framework.rpc.teleapi.http.HttpRpcExchange;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;

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
