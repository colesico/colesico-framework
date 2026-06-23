package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.TeleHttpResponse;
import jakarta.inject.Provider;

abstract public class TeleHttpResponseWriter<V extends TeleHttpResponse, O extends HttpWriteOptions> implements HttpTeleWriter<V, O> {

    protected final Provider<HttpResponse> httpResponse;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    abstract protected Integer defaultStatusCode(V value, Class<V> valueType, O options);

    abstract protected String defaultContentType(V value, Class<V> valueType, O options);

    abstract protected void sendValue(HttpResponse response, V value, Class<V> valueType, O options, Integer statusCode, String contentType);

    @Override
    public void write(V value, Class<V> valueType, O options) {

        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(204).sendText("");
            return;
        }

        var statusCode = value.statusCode();
        if (statusCode == null) {
            statusCode = options.statusCode();
            if (statusCode == null) {
                statusCode = defaultStatusCode(value, valueType, options);
            }
        }
        response.setStatus(statusCode);

        var contentType = value.contentType();
        if (contentType == null) {
            contentType = options.contentType();
            if (contentType == null) {
                contentType = defaultContentType(value, valueType, options);
            }
        }

        response.setContentType(contentType);

        if (!value.headers().isEmpty()) {
            HttpUtils.setHeaders(response, value.headers());
        }

        if (!value.cookies().isEmpty()) {
            HttpUtils.setCookies(response, value.cookies());
        }

        sendValue(response, value, valueType, options, statusCode, contentType);
    }
}
