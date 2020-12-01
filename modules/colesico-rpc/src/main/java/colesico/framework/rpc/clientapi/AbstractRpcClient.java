package colesico.framework.rpc.clientapi;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.RpcName;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RPC client base
 */
abstract public class AbstractRpcClient implements RpcClient {

    protected final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private final EndpointsRegistryImpl endpoints = new EndpointsRegistryImpl();

    private final List<RpcRequestHandler<?>> requestHandlers = new ArrayList<>();

    public AbstractRpcClient(Polysupplier<RpcEndpointsPrototype> endpointsConf,
                             Polysupplier<RpcRequestHandler<?>> reqHandlers) {
        endpointsConf.forEach(c -> c.addEndpoints(endpoints), null);
        reqHandlers.forEach(c -> this.requestHandlers.add(c), null);

        if (logger.isDebugEnabled()) {
            StringWriter writer = new StringWriter();
            endpoints.dump(writer);
            logger.debug("RPC endpoints:\n" + writer.toString());
        }
    }

    abstract protected <T> void serialize(T obj, OutputStream os);

    abstract protected <T> T deserialize(InputStream is, Class<T> type);

    abstract protected InputStream callEndpoint(String endpoint, String rpcApiName, String rpcMethodName, byte[] data);

    @Override
    public <R> RpcResponse<R> call(String rpcApiName, String rpcMethodName, RpcRequest request, Class<? extends RpcResponse<R>> responseType) {
        logger.debug("RPC client calls api: {} method: {}", rpcApiName, rpcMethodName);

        // Invoke request handlers
        for (RpcRequestHandler reqHandler : requestHandlers) {
            reqHandler.onRequest(request);
        }

        // Serialize request
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        serialize(request, os);
        byte[] requestData = os.toByteArray();
        logger.debug("Request data size: {} bytes", requestData.length);

        // Resolve endpoint
        String endpoint = resolveEndpoint(rpcApiName);
        logger.debug("Resolved endpoint {}", endpoint);

        // Call endpoint
        InputStream responseStream = callEndpoint(endpoint, rpcApiName, rpcMethodName, requestData);

        // Deserialize response
        RpcResponse<R> rpcResponse = deserialize(responseStream, responseType);

        // Invoke response handlers
        // TODO:

        return rpcResponse;
    }

    protected String resolveEndpoint(String rpcApiName) {
        String endpoint = endpoints.getEndpoint(rpcApiName);
        if (endpoint == null) {
            throw new RpcException("RPC API " + rpcApiName + " endpoint is not defined");
        }
        return endpoint;
    }

    private static final class EndpointsRegistryImpl implements RpcEndpointsPrototype.EndpointsRegistry {

        // RPC API Name to endpoint map
        private final Map<String, String> endpointsMap = new HashMap<>();

        @Override
        public void addEndpoint(String apcApiName, String endpoint) {
            String prev = endpointsMap.put(apcApiName, endpoint);
            if (prev != null) {
                throw new RpcException("RPC API " + apcApiName + " endpoint already defined: " + prev);
            }
        }

        @Override
        public void addEndpoint(Class<?> apcApiClass, String endpoint) {
            RpcName rpcName = apcApiClass.getAnnotation(RpcName.class);
            if (rpcName != null) {
                addEndpoint(rpcName.value(), endpoint);
            } else {
                addEndpoint(apcApiClass.getCanonicalName(), endpoint);
            }
        }

        public String getEndpoint(String rpcApiName) {
            return endpointsMap.get(rpcApiName);
        }

        public void dump(StringWriter writer) {
            for (Map.Entry<String, String> en : endpointsMap.entrySet()) {
                writer.append("RPC endpoint: ").append(en.getKey()).append(" -> ").append(en.getValue());
            }
        }
    }
}
