package colesico.framework.fusionhttp.internal;

import colesico.framework.http.*;
import io.fusionauth.http.server.HTTPRequest;

import java.io.InputStream;
import java.io.Writer;

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
        return new HttpValues<>(request.getHeaders());
    }

    @Override
    public HttpValues<String, HttpCookie> cookies() {
        HttpValues.Builder<String, HttpCookie> builder = HttpValues.builder();
        for (var cookie : request.getCookies()) {
            builder.add(cookie.getName(), new FusionCookie(cookie));
        }
        return builder.build();
    }

    @Override
    public HttpValues<String, String> queryParameters() {
        return new HttpValues<>(request.getURLParameters());
    }

    @Override
    public HttpValues<String, String> formData() {
        return new HttpValues<>(request.getFormData());
    }

    @Override
    public HttpValues<String, HttpFile> files() {
        HttpValues.Builder<String, HttpFile> builder = HttpValues.builder();
        for (var file : request.getFiles()) {
            builder.add(file.getName(), new FusionHttpFile(file));
        }
        return builder.build();
    }

    @Override
    public InputStream inputStream() {
        return request.getInputStream();
    }

    @Override
    public void dump(Writer out) {
        try {
            out.write("HTTP Request");
            out.write(request.getMethod().name() + " ");
            out.write(request.getBaseURL() + request.getPath() + request.getQueryString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
