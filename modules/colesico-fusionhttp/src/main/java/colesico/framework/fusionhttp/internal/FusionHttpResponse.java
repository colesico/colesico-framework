package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import io.fusionauth.http.Cookie;
import io.fusionauth.http.server.HTTPResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class FusionHttpResponse implements HttpResponse {

    private final HTTPResponse response;

    public FusionHttpResponse(HTTPResponse response) {
        this.response = response;
    }

    @Override
    public HttpResponse setStatus(Integer code) {
        response.setStatus(code);
        return this;
    }

    @Override
    public Integer getStatus() {
        return response.getStatus();
    }

    @Override
    public HttpResponse setHeader(String name, String vale) {
        response.setHeader(name, vale);
        return this;
    }

    @Override
    public HttpResponse addHeader(String name, String vale) {
        response.addHeader(name, vale);
        return this;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return response.getHeadersMap();
    }

    @Override
    public HttpResponse addCookie(HttpCookie cookie) {
        response.addCookie(((FusionHttpCookie) cookie).unwrap());
        return this;
    }

    @Override
    public List<HttpCookie> getCookies() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public OutputStream outputStream() {
        return response.getOutputStream();
    }

    @Override
    public void close() {
        try {
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isCommitted() {
        return response.isCommitted();
    }

    @Override
    public void dump(Writer out) {
        try {
            out.write("HTTP Response");
            out.write("\nstatus: " + response.getStatus());
            out.write("\ncontent-type: " + response.getContentType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
