package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import io.fusionauth.http.server.HTTPResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

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
    public HttpResponse setContentType(String contentType) {
        response.setContentType(contentType);
        return this;
    }

    @Override
    public HttpResponse setCookie(HttpCookie cookie) {
        return this;
    }

    @Override
    public HttpResponse setHeader(String name, String vale) {
        response.setHeader(name, vale);
        return this;
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
        // TODO:...
    }
}
