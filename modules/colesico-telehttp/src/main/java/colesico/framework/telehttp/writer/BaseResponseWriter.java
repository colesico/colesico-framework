package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.HttpTeleError;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.BaseResponse;
import jakarta.inject.Provider;

import java.io.*;

/**
 * General {@link BaseResponse} writer
 */
abstract public class BaseResponseWriter<V extends BaseResponse, O extends HttpWriteOptions> implements HttpWriter<V, O> {

    protected final Provider<HttpResponse> httpResponse;

    public BaseResponseWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * Implement this method to write to response output stream
     *
     * @param outputStream do not close after write
     */
    abstract protected void write(OutputStream outputStream, V response, O options) throws IOException;

    protected Integer statusCode(V response, O options, Integer defaultValue) {
        if (response.statusCode() != null) {
            return response.statusCode();
        }
        if (options.statusCode() != null) {
            return options.statusCode();
        }
        return defaultValue;
    }

    protected Integer emptyStatusCode(V response, O options) {
        return 204;
    }

    protected ContentType contentType(V response, O options, ContentType defaultValue) {
        if (response.contentType() != null) {
            return response.contentType();
        }
        if (options.contentType() != null) {
            return options.contentType();
        }
        return defaultValue;
    }

    protected boolean isEmptyResponse(V response) {
        return response == null;
    }

    @Override
    public void write(V response, O options) {

        var httpResponse = this.httpResponse.get();

        if (httpResponse.isCommitted()) {
            throw HttpTeleError.of("HTTP Response is committed while writing response", 500, response);
        }

        if (isEmptyResponse(response)) {
            httpResponse.setStatus(emptyStatusCode(response, options)).close();
            return;
        }

        var statusCode = statusCode(response, options, 200);
        if (statusCode == null) {
            throw HttpTeleError.of("Undefined http status code", 500);
        }

        var contentType = contentType(response, options, ContentType.TEXT_PLAIN);
        if (contentType == null) {
            throw HttpTeleError.of("Undefined content type", 500);
        }

        httpResponse.setStatus(statusCode).setContentType(contentType.headerValue());

        if (!response.headers().isEmpty()) {
            HttpUtils.setHeaders(httpResponse, response.headers());
        }

        if (!response.cookies().isEmpty()) {
            HttpUtils.setCookies(httpResponse, response.cookies());
        }

        // Write response to intermediate buffer first; if successful, copy to output stream.
        // This keeps output stream open for error handling in caller.
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            write(buffer, response, options);
            var outputStream = httpResponse.outputStream();
            buffer.writeTo(outputStream);
            // Do not close outputStream here, will be closed in http server handler
            outputStream.flush();
        } catch (Exception e) {
            throw HttpTeleError.of(e, 500);
        }
    }


}
