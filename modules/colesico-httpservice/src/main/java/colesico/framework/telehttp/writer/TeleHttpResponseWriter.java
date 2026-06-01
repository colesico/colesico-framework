package colesico.framework.telehttp.writer;

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

        HttpUtils.setHeaders(response, value.headers());
        HttpUtils.setCookies(response, value.cookies());

        response.setStatus(value.statusCode());
        response.setContentType(value.contentType());
    }
}
