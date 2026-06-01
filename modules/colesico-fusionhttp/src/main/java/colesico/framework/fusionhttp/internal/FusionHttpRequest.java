package colesico.framework.fusionhttp.internal;

import colesico.framework.http.*;
import io.fusionauth.http.server.HTTPRequest;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FusionHttpRequest implements HttpRequest {
    private final HTTPRequest request;

    public FusionHttpRequest(HTTPRequest request) {
        this.request = request;
    }

    @Override
    public HttpMethod method() {
        return HttpMethod.of(request.getMethod().name());
    }

    @Override
    public String scheme() {
        return request.getScheme();
    }

    @Override
    public String host() {
        return request.getHost();
    }

    @Override
    public Integer port() {
        return request.getPort();
    }

    @Override
    public String path() {
        return request.getPath();
    }

    @Override
    public String queryString() {
        return request.getQueryString();
    }

    @Override
    public HttpValues<String, String> headers() {
        var headers = request.getHeaders();
        Map<String, MultiValue<String>> result = new HashMap<>();
        headers.forEach((name,values)->{

            var multiValue = result.computeIfAbsent(name, new MultiValue<>());
        });

        return new HttpValues<>(result);
    }

    @Override
    public HttpValues<String, HttpCookie> cookies() {
        return null;
    }

    @Override
    public HttpValues<String, String> queryParameters() {
        return null;
    }

    @Override
    public HttpValues<String, String> postParameters() {
        return null;
    }

    @Override
    public HttpValues<String, HttpFile> postFiles() {
        return null;
    }

    @Override
    public InputStream inputStream() {
        return request.getInputStream();
    }

    @Override
    public void dump(Writer out) {

    }
}
