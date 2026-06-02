package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import io.fusionauth.http.server.HTTPResponse;

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
    public void sendText(String text) {
        try (var writer = response.getWriter()) {
            writer.write(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendData(ByteBuffer buffer) {
        try (OutputStream os = response.getOutputStream()) {
            if (buffer.hasArray()) {
                os.write(
                        buffer.array(),
                        buffer.arrayOffset() + buffer.position(),
                        buffer.remaining()
                );
                buffer.position(buffer.limit());
            } else {
                WritableByteChannel channel = Channels.newChannel(os);

                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendRedirect(String location) {
        response.sendRedirect(location);
    }

    @Override
    public OutputStream outputStream() {
        return response.getOutputStream();
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
