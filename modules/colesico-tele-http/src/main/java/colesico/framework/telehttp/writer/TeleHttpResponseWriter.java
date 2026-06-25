package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.TeleHttpResponse;
import jakarta.inject.Provider;

/**
 * General {@link TeleHttpResponse} writer
 */
abstract public class TeleHttpResponseWriter<R extends TeleHttpResponse, O extends TeleHttpWriteOptions> implements TeleHttpWriter<R, O> {

    public static final Integer DEFAULT_STATUS_CODE = 200;

    protected final Provider<HttpResponse> httpResponse;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    abstract protected void writeResponse(HttpResponse protocol,
                                          R response,
                                          O options,
                                          Integer effectiveStatusCode,
                                          MediaType effectiveMediaType);

    protected Integer statusCode(R response, O options, Integer defaultValue) {
        if (response.statusCode() != null) {
            return response.statusCode();
        }
        if (options.statusCode() != null) {
            return options.statusCode();
        }
        return defaultValue;
    }

    protected MediaType mediaType(R response, O options, MediaType defaultValue) {
        if (response.mediaType() != null) {
            return response.mediaType();
        }
        if (options.mediaType() != null) {
            return options.mediaType();
        }
        return defaultValue;
    }

    protected String mediaTypeToContentType(MediaType mediaType) {
        if (mediaType == null) {
            return "";
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
            protocol.setStatus(204).sendText("");
            return;
        }

        var statusCode = statusCode(response, options, DEFAULT_STATUS_CODE);
        protocol.setStatus(statusCode);

        var mediaType = mediaType(response, options, null);
        if (mediaType != null) {
            protocol.setContentType(mediaTypeToContentType(mediaType));
        }

        if (!response.headers().isEmpty()) {
            HttpUtils.setHeaders(protocol, response.headers());
        }

        if (!response.cookies().isEmpty()) {
            HttpUtils.setCookies(protocol, response.cookies());
        }

        writeResponse(protocol, response, options, statusCode, mediaType);
    }
}
