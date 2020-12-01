package colesico.framework.rpc.teleapi.http;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.client.AbstractRpcClient;
import colesico.framework.rpc.teleapi.client.RpcEndpointsPrototype;
import colesico.framework.rpc.teleapi.client.RpcRequestHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

abstract public class HttpRpcClient extends AbstractRpcClient {

    public static final String RPC_API_HEADER = "X-RPC-API";
    public static final String RPC_METHOD_HEADER = "X-RPC-Method";
    public static final String RPC_ERROR_HEADER = "X-RPC-Error";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String RPC_CONTENT_TYPE = "application/octet-stream";

    protected final HttpClient httpClient;

    public HttpRpcClient(Polysupplier<RpcEndpointsPrototype> endpointsConf,
                         Polysupplier<HttpRpcClientOptionsPrototype> options,
                         Polysupplier<RpcRequestHandler<?>> reqHandlers) {
        super(endpointsConf, reqHandlers);
        final HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2);

        options.forEach(o -> o.applyOptions(httpClientBuilder), null);

        httpClient = httpClientBuilder.build();
    }

    @Override
    protected InputStream callEndpoint(String endpoint, String rpcApiName, String rpcMethodName, byte[] data) {
        try {
            HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofByteArray(data);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header(CONTENT_TYPE_HEADER, RPC_CONTENT_TYPE)
                    .header(RPC_API_HEADER, URLEncoder.encode(rpcApiName, StandardCharsets.UTF_8))
                    .header(RPC_METHOD_HEADER, URLEncoder.encode(rpcMethodName, StandardCharsets.UTF_8))
                    .POST(publisher)
                    .build();

            HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
            return response.body();
        } catch (Exception e) {
            throw new RpcException("PRC Server invocation error: " + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }
}
