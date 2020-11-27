package colesico.framework.rpc.internal.kryo;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.teleapi.http.HttpRpcClient;
import colesico.framework.rpc.teleapi.http.HttpRpcClientOptionsPrototype;

import java.io.InputStream;
import java.io.OutputStream;

public class KryoClient extends HttpRpcClient {

    private final KryoSerializer serializer;

    public KryoClient(Polysupplier<HttpRpcClientOptionsPrototype> options, KryoSerializer serializer) {
        super(options);
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
