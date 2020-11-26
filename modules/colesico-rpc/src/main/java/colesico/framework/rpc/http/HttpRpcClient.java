package colesico.framework.rpc.http;

import colesico.framework.rpc.client.RpcClient;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

abstract public class HttpRpcClient implements RpcClient {

    public static final String RPC_API_HEADER = "X-RPC-API";
    public static final String RPC_METHOD_HEADER = "X-RPC-Method";
    public static final String RPC_ERROR_HEADER = "X-RPC-Error";

    @Override
    public <R> RpcResponse<R> serve(String apiName, String methodName, RpcRequest request, Class<? extends RpcResponse<R>> responseType) {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .proxy(ProxySelector.of(new InetSocketAddress("www-proxy.com", 8080)))
                .authenticator(Authenticator.getDefault())
                .build();

      //  HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofInputStream();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://"))
                //.POST(publisher)
                .build();

        //HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        return null;
    }
}
