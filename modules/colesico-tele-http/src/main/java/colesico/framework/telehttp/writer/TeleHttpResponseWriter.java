package colesico.framework.telehttp.writer;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.TeleHttpResponse;
import jakarta.inject.Provider;

abstract public class TeleHttpResponseWriter<V extends TeleHttpResponse, W extends HttpWriteOptions> implements HttpTeleWriter<V, W> {

    protected final Provider<HttpResponse> httpResponse;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(V value, Class<V> valueType, W options) {

        final var response = httpResponse.get();

        if (!value.headers().isEmpty()) {
            HttpUtils.setHeaders(response, value.headers());
        }

        if (!value.cookies().isEmpty()) {
            HttpUtils.setCookies(response, value.cookies());
        }

        if (value.statusCode() != 0) {
            response.setStatus(value.statusCode());
        }

        if (!StringUtils.isBlank(value.contentType())) {
            response.setContentType(value.contentType());
        }
    }
}
