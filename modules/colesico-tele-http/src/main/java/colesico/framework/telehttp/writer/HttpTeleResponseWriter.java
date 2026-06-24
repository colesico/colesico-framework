package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.HttpTeleResponse;
import jakarta.inject.Provider;

/**
 * General {@link HttpTeleResponse} writer
 */
abstract public class HttpTeleResponseWriter<V extends HttpTeleResponse, O extends HttpWriteOptions> implements HttpTeleWriter<V, O> {

    public static final Integer DEFAULT_STATUS_CODE = 200;
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";

    protected final Provider<HttpResponse> httpResponse;

    public HttpTeleResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    abstract protected void writeValue(HttpResponse response, V value, O options,
                                       Integer effectiveStatusCode,
                                       String effectiveContentType);

    protected Integer defaultStatusCode(V value, O options) {
        return DEFAULT_STATUS_CODE;
    }

    protected String defaultContentType(V value, O options) {
        return DEFAULT_CONTENT_TYPE;
    }

    @Override
    public void write(V value, O options) {

        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(204).sendText("");
            return;
        }

        var statusCode = value.statusCode();
        if (statusCode == null) {
            statusCode = options.statusCode();
            if (statusCode == null) {
                statusCode = defaultStatusCode(value, options);
            }
        }
        response.setStatus(statusCode);

        var contentType = value.contentType();
        if (contentType == null) {
            contentType = options.contentType();
            if (contentType == null) {
                contentType = defaultContentType(value, options);
            }
        }

        response.setContentType(contentType);

        if (!value.headers().isEmpty()) {
            HttpUtils.setHeaders(response, value.headers());
        }

        if (!value.cookies().isEmpty()) {
            HttpUtils.setCookies(response, value.cookies());
        }

        writeValue(response, value, options, statusCode, contentType);
    }
}
