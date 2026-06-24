package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.TeleHttpResponse;
import jakarta.inject.Provider;

/**
 * General {@link TeleHttpResponse} writer
 */
abstract public class TeleHttpResponseWriter<R extends TeleHttpResponse, O extends HttpWriteOptions> implements TeleHttpWriter<R, O> {

    public static final Integer DEFAULT_STATUS_CODE = 200;

    protected final Provider<HttpResponse> httpResponse;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    abstract protected void writeResponse(HttpResponse protocol,
                                          R response,
                                          O options,
                                          Integer effectiveStatusCode,
                                          String effectiveContentType);

    protected Integer statusCode(R response, O options, Integer defaultValue) {
        if (response.statusCode() != null) {
            return response.statusCode();
        }
        if (options.statusCode() != null) {
            return options.statusCode();
        }
        return defaultValue;
    }

    protected String contentType(R response, O options, String defaultValue) {
        if (response.contentType() != null) {
            return response.contentType();
        }
        if (options.contentType() != null) {
            return options.contentType();
        }
        return defaultValue;
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

        var contentType = contentType(response, options, null);
        if (contentType != null) {
            protocol.setContentType(contentType);
        }

        if (!response.headers().isEmpty()) {
            HttpUtils.setHeaders(protocol, response.headers());
        }

        if (!response.cookies().isEmpty()) {
            HttpUtils.setCookies(protocol, response.cookies());
        }

        writeResponse(protocol, response, options, statusCode, contentType);
    }
}
