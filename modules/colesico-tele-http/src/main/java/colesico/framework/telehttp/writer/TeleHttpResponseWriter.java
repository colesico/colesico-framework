package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.TeleHttpResponse;
import jakarta.inject.Provider;

/**
 * General {@link TeleHttpResponse} writer
 */
abstract public class TeleHttpResponseWriter<R extends TeleHttpResponse, O extends TeleHttpWriteOptions> implements TeleHttpWriter<R, O> {

    protected final Provider<HttpResponse> httpResponse;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    abstract protected void writeResponse(HttpResponse protocol,
                                          R response,
                                          O options,
                                          Integer statusCode,
                                          MediaType mediaType);

    abstract protected MediaType defaultMediaType();

    protected Integer defaultStatusCode() {
        return 200;
    }

    protected Integer emptyStatusCode() {
        return 204;
    }

    protected Integer statusCode(R response, O options) {
        if (response.statusCode() != null) {
            return response.statusCode();
        }
        if (options.statusCode() != null) {
            return options.statusCode();
        }
        return defaultStatusCode();
    }

    protected MediaType mediaType(R response, O options) {
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

    @Override
    public void write(R response, O options) {

        var protocol = httpResponse.get();

        if (response == null) {
            protocol.setStatus(emptyStatusCode()).close();
            return;
        }

        var statusCode = statusCode(response, options);
        if (statusCode == null) {
            throw TeleHttpException.of("Undefined http status code", 500);
        }

        var mediaType = mediaType(response, options);

        if (!response.headers().isEmpty()) {
            HttpUtils.setHeaders(protocol, response.headers());
        }

        if (!response.cookies().isEmpty()) {
            HttpUtils.setCookies(protocol, response.cookies());
        }

        writeResponse(protocol, response, options, statusCode, mediaType);
    }


}
