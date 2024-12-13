package colesico.framework.rpc.clientapi;

import colesico.framework.assist.StrUtils;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
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

    private final List<RpcRequestHandler> requestHandlers = new ArrayList<>();
    private final List<RpcResponseHandler> responseHandlers = new ArrayList<>();

    private final RpcErrorHandlerFactory errorHandlerFactory;

    public AbstractRpcClient(Polysupplier<RpcEndpointsPrototype> endpointsConf,
                             Polysupplier<RpcRequestHandler> requestHnd,
                             Polysupplier<RpcResponseHandler> responseHnd,
                             RpcErrorHandlerFactory errorHndFac) {

        endpointsConf.forEach(c -> c.addEndpoints(endpoints));
        requestHnd.forEach(c -> this.requestHandlers.add(c));
        responseHnd.forEach(c -> this.responseHandlers.add(c));

        this.errorHandlerFactory = errorHndFac;

        if (logger.isDebugEnabled()) {
            StringWriter writer = new StringWriter();
            endpoints.dump(writer);
            logger.debug("RPC endpoints:\n" + writer.toString());
        }
    }

    abstract protected <T> void serialize(T obj, OutputStream os);

    abstract protected <T> T deserialize(InputStream is, Class<T> type);

    abstract protected EndpointResponse callEndpoint(String endpoint, String rpcNamespace, String rpcApiName, String rpcMethodName, byte[] data);

    @Override
    public <R> RpcResponse<R> call(String rpcNamespace, String rpcApiName, String rpcMethodName, RpcRequest request, Class<? extends RpcResponse<R>> responseType) {
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
        EndpointResponse endpointResp = callEndpoint(endpoint, rpcNamespace, rpcApiName, rpcMethodName, requestData);

        if (endpointResp.getError() != null) {
            throw createException(endpointResp.getError());
        }

        InputStream responseStream = endpointResp.getInputStream();

        // Deserialize response
        RpcResponse<R> response = deserialize(responseStream, responseType);

        // Handle error
        if (response.getError() != null) {
            throw createException(endpointResp.getError());
        }

        // Invoke response handlers
        for (RpcResponseHandler respHandler : responseHandlers) {
            respHandler.onResponse(response);
        }

        return response;
    }

    protected RuntimeException createException(RpcError err) {
        RpcErrorHandler errHandler = errorHandlerFactory.getErrorHandler(err.getClass());

        if (errHandler != null) {
            return errHandler.createException(err);
        }

        throw new RpcException("Undefined error handler for " + err);
    }

    protected String resolveEndpoint(String rpcApiName) {
        String endpoint = endpoints.getEndpoint(rpcApiName);
        if (endpoint == null) {
            throw new RpcException("RPC API " + rpcApiName + " endpoint is not defined");
        }
        return endpoint;
    }

    private static final class EndpointsRegistryImpl implements RpcEndpointsPrototype.EndpointsRegistry {
        protected final Logger logger = LoggerFactory.getLogger(EndpointsRegistryImpl.class);
        // RPC API Name to endpoint map
        private final Map<String, String> endpointsMap = new HashMap<>();

        @Override
        public void addEndpoint(String rpcApiName, String endpoint) {
            logger.debug("Add RPC API name: {} -> Endpoint: {}", rpcApiName, endpoint);
            String prev = endpointsMap.put(rpcApiName, endpoint);
            if (prev != null) {
                throw new RpcException("RPC API " + rpcApiName + " endpoint already defined as:" + prev + " while trying to register: " + endpoint);
            }
        }

        @Override
        public void addEndpoint(Class<?> rpcApiClass, String endpoint) {
            RpcApi rpcApiAnn = rpcApiClass.getAnnotation(RpcApi.class);
            if (rpcApiAnn == null) {
                throw new RpcException("Not a RPC interface: " + rpcApiClass.getCanonicalName());
            }
            if (StrUtils.isEmpty(rpcApiAnn.name())) {
                addEndpoint(rpcApiClass.getCanonicalName(), endpoint);
            } else {
                addEndpoint(rpcApiAnn.name(), endpoint);
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

    public static class EndpointResponse {
        private final InputStream inputStream;
        private final RpcError error;

        private EndpointResponse(InputStream inputStream, RpcError error) {
            this.inputStream = inputStream;
            this.error = error;
        }

        public static EndpointResponse error(RpcError err) {
            return new EndpointResponse(null, err);
        }

        public static EndpointResponse success(InputStream inputStream) {
            return new EndpointResponse(inputStream, null);
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public RpcError getError() {
            return error;
        }
    }
}
