package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.*;
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

    /**
     *  Calculate result http status
     */
    protected Integer status(V response, O options, Integer defaultStatus) {
        if (response.status() != null) {
            return response.status();
        }
        if (options.status() != null) {
            return options.status();
        }
        return defaultStatus;
    }

    protected Integer emptyStatus(V response, O options) {
        return 204;
    }

    protected ContentType contentType(V response, O options, ContentType defaultContentType) {
        if (response.contentType() != null) {
            return response.contentType();
        }
        if (options.contentType() != null) {
            return options.contentType();
        }
        return defaultContentType;
    }

    protected boolean isEmptyResponse(V response) {
        return response == null;
    }

    @Override
    public void write(V response, O options) {

        var httpResponse = this.httpResponse.get();

        if (httpResponse.isCommitted()) {
            throw new HttpTeleException("HTTP Response is committed while writing response");
        }

        if (isEmptyResponse(response)) {
            httpResponse.setStatus(emptyStatus(response, options)).close();
            return;
        }

        var status = status(response, options, 200);
        if (status == null) {
            throw new HttpTeleException("Undefined http status");
        }

        var contentType = contentType(response, options, ContentType.TEXT_PLAIN);
        if (contentType == null) {
            throw new HttpTeleException("Undefined content type");
        }

        httpResponse.setStatus(status).setContentType(contentType.headerValue());

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
            throw new HttpTeleException(e);
        }
    }


}
