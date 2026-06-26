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
    protected final WriterOptions writerOptions;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse,
                                  WriterOptions writerOptions) {
        this.httpResponse = httpResponse;
        this.writerOptions = writerOptions;
    }

    abstract protected void writeResponse(HttpResponse protocol,
                                          R response,
                                          O options,
                                          Integer statusCode,
                                          MediaType mediaType);

    protected Integer statusCode(R response, O options) {
        if (response.statusCode() != null) {
            return response.statusCode();
        }
        if (options.statusCode() != null) {
            return options.statusCode();
        }
        return writerOptions.statusCode;
    }

    protected MediaType mediaType(R response, O options) {
        if (response.mediaType() != null) {
            return response.mediaType();
        }
        if (options.mediaType() != null) {
            return options.mediaType();
        }
        return writerOptions.mediaType;
    }

    protected String mediaTypeToContentType(MediaType mediaType) {
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
            protocol.setStatus(204).close();
            return;
        }

        var statusCode = statusCode(response, options);
        if (statusCode == null) {
            throw TeleHttpException.of("Undefined http status code", 500);
        }
        protocol.setStatus(statusCode);

        var mediaType = mediaType(response, options);
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

    /**
     * Writer options
     */
    public static class WriterOptions {
        final Integer statusCode;
        final MediaType mediaType;

        public WriterOptions(Integer statusCode, MediaType mediaType) {
            this.statusCode = statusCode;
            this.mediaType = mediaType;
        }

        public Integer statusCode() {
            return statusCode;
        }

        public MediaType mediaType() {
            return mediaType;
        }
    }
}
