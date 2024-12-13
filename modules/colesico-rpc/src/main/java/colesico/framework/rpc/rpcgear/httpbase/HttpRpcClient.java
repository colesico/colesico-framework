package colesico.framework.rpc.rpcgear.httpbase;

import colesico.framework.assist.StrUtils;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.clientapi.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Http based client
 */
abstract public class HttpRpcClient extends AbstractRpcClient {

    public static final String RPC_NAMESPACE_HEADER = "X-RPC-NS";
    public static final String RPC_API_HEADER = "X-RPC-API";
    public static final String RPC_METHOD_HEADER = "X-RPC-Method";
    public static final String RPC_ERROR_HEADER = "X-RPC-Error";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String RPC_CONTENT_TYPE = "application/octet-stream";

    protected final HttpClient httpClient;

    public HttpRpcClient(Polysupplier<RpcEndpointsPrototype> endpointsConf,
                         Polysupplier<HttpRpcClientOptionsPrototype> options,
                         Polysupplier<RpcRequestHandler> requestHnd,
                         Polysupplier<RpcResponseHandler> responseHnd,
                         RpcErrorHandlerFactory errorHndFac) {
        super(endpointsConf, requestHnd, responseHnd, errorHndFac);
        final HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2);

        options.forEach(o -> o.applyOptions(httpClientBuilder));

        httpClient = httpClientBuilder.build();
    }

    @Override
    protected EndpointResponse callEndpoint(String endpoint, String rpcNamespace, String rpcApiName, String rpcMethodName, byte[] data) {
        try {
            HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofByteArray(data);

            if (StrUtils.isEmpty(rpcNamespace)){
                rpcNamespace = RpcApi.DEFAULT_NAMESPACE;
            }

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header(CONTENT_TYPE_HEADER, RPC_CONTENT_TYPE)
                    .header(RPC_NAMESPACE_HEADER, URLEncoder.encode(rpcNamespace, StandardCharsets.UTF_8))
                    .header(RPC_API_HEADER, URLEncoder.encode(rpcApiName, StandardCharsets.UTF_8))
                    .header(RPC_METHOD_HEADER, URLEncoder.encode(rpcMethodName, StandardCharsets.UTF_8))
                    .POST(publisher)
                    .build();

            HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

            Optional<String> errorHeader = response.headers().firstValue(RPC_ERROR_HEADER);
            if (errorHeader.isPresent()) {
                return EndpointResponse.error(parseErrorHeader(errorHeader.get()));
            }

            return EndpointResponse.success(response.body());
        } catch (Exception e) {
            throw new RpcException("PRC Server invocation error: " + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    private RpcError parseErrorHeader(String errStr) {
        if (StringUtils.isEmpty(errStr)) {
            return null;
        }

        int sepIdx = errStr.indexOf(':');

        String exceptionType = errStr.substring(0, sepIdx);
        if ("".equals(exceptionType)) {
            exceptionType = null;
        }

        String message = errStr.substring(sepIdx + 1);
        if ("".equals(message)) {
            message = null;
        }

        return RpcError.of(exceptionType, message);
    }
}
