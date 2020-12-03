package colesico.framework.rpc.rpcgear.kryo;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.clientapi.RpcEndpointsPrototype;
import colesico.framework.rpc.clientapi.RpcErrorHandler;
import colesico.framework.rpc.clientapi.RpcErrorHandlerFactory;
import colesico.framework.rpc.clientapi.RpcRequestHandler;
import colesico.framework.rpc.rpcgear.httpbase.HttpRpcClient;
import colesico.framework.rpc.rpcgear.httpbase.HttpRpcClientOptionsPrototype;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;

@Singleton
public class KryoClient extends HttpRpcClient {

    private final KryoSerializer serializer;

    @Inject
    public KryoClient(Polysupplier<RpcEndpointsPrototype> endpointsConf,
                      Polysupplier<HttpRpcClientOptionsPrototype> options,
                      Polysupplier<RpcRequestHandler<?>> requestHnd,
                      RpcErrorHandlerFactory errorHndFac,
                      KryoSerializer serializer) {
        super(endpointsConf, options, requestHnd, errorHndFac);
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
