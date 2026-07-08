package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.HttpTeleException;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.TeleHttpResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;

/**
 * General {@link TeleHttpResponse} writer
 */
abstract public class TeleHttpResponseWriter<V extends TeleHttpResponse, O extends HttpWriteOptions> implements HttpWriter<V, O> {

    protected final Provider<HttpResponse> httpResponse;

    public TeleHttpResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    abstract protected ContentType defaultContentType();

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

    protected ContentType contentType(V response, O options) {
        if (response.contentType() != null) {
            return response.contentType();
        }
        if (options.contentType() != null) {
            return options.contentType();
        }
        return defaultContentType();
    }

    protected boolean isEmptyResponse(V response) {
        return response == null;
    }

    @Override
    public void write(V response, O options) {

        var httpResponse = this.httpResponse.get();

        if (httpResponse.isCommitted()) {
            throw HttpTeleException.of("HTTP Response is committed while writing response", 500, response);
        }

        if (isEmptyResponse(response)) {
            httpResponse.setStatus(emptyStatusCode()).close();
            return;
        }

        var statusCode = statusCode(response, options);
        if (statusCode == null) {
            throw HttpTeleException.of("Undefined http status code", 500);
        }

        var contentType = contentType(response, options);
        if (contentType == null) {
            throw HttpTeleException.of("Undefined media type", 500);
        }

        httpResponse.setStatus(statusCode).setContentType(contentType.headerValue());

        if (!response.headers().isEmpty()) {
            HttpUtils.setHeaders(httpResponse, response.headers());
        }

        if (!response.cookies().isEmpty()) {
            HttpUtils.setCookies(httpResponse, response.cookies());
        }

        try (OutputStream os = httpResponse.outputStream()) {
            write(os, response, options);
            os.flush();
        } catch (Exception e) {
            throw HttpTeleException.of(e, 500);
        }
    }


}
