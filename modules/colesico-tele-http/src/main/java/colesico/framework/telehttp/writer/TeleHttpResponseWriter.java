package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.TeleHttpResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;

/**
 * General {@link TeleHttpResponse} writer
 */
abstract public class TeleHttpResponseWriter<V extends TeleHttpResponse, O extends TeleHttpWriteOptions> implements TeleHttpWriter<V, O> {

    protected final Provider<HttpResponse> httpResponse;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    abstract protected MediaType defaultMediaType();

    abstract protected void write(OutputStream outputStream, V response, O options) throws IOException;

    protected Integer defaultStatusCode() {
        return 200;
    }

    protected Integer emptyStatusCode() {
        return 204;
    }

    protected Integer statusCode(V response, O options) {
        if (response.statusCode() != null) {
            return response.statusCode();
        }
        if (options.statusCode() != null) {
            return options.statusCode();
        }
        return defaultStatusCode();
    }

    protected MediaType mediaType(V response, O options) {
        if (response.mediaType() != null) {
            return response.mediaType();
        }
        if (options.mediaType() != null) {
            return options.mediaType();
        }
        return defaultMediaType();
    }

    protected String toContentType(MediaType mediaType) {
        if (mediaType == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(mediaType.mimeType());
        mediaType.parameters().forEach((name, value) -> {
            result.append("; ").append(name).append("=").append(value);
        });

        return result.toString();
    }

    protected boolean isEmptyResponse(V response) {
        return response == null;
    }

    @Override
    public void write(V response, O options) {

        var protocol = httpResponse.get();

        if (isEmptyResponse(response)) {
            protocol.setStatus(emptyStatusCode()).close();
            return;
        }

        var statusCode = statusCode(response, options);
        if (statusCode == null) {
            throw TeleHttpException.of("Undefined http status code", 500);
        }

        var mediaType = mediaType(response, options);
        if (mediaType == null) {
            throw TeleHttpException.of("Undefined media type", 500);
        }

        protocol.setStatus(statusCode).setContentType(toContentType(mediaType));

        if (!response.headers().isEmpty()) {
            HttpUtils.setHeaders(protocol, response.headers());
        }

        if (!response.cookies().isEmpty()) {
            HttpUtils.setCookies(protocol, response.cookies());
        }

        try (OutputStream os = protocol.outputStream()) {
            write(os, response, options);
            os.flush();
        } catch (Exception e) {
            throw TeleHttpException.of(e, 500);
        }
    }


}
