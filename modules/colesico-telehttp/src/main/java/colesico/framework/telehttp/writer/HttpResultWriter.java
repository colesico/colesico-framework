package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.telehttp.*;
import colesico.framework.telehttp.result.HttpResult;
import jakarta.inject.Provider;

import java.io.*;

/**
 * Basic {@link HttpResult}  writer
 */
abstract public class HttpResultWriter<R extends HttpResult, O extends HttpWriteOptions>
        implements HttpWriter<R, O> {

    protected final Provider<HttpResponse> httpResponse;

    public HttpResultWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * Implement this method to write to response output stream
     *
     * @param outputStream do not close after write
     */
    abstract protected void write(OutputStream outputStream, R result, O options) throws IOException;

    /**
     * Calculate resulting http status
     */
    protected Integer status(R result, O options, Integer defaultStatus) {
        if (result.status() != null) {
            return result.status();
        }

        if (options.status() != null) {
            return options.status();
        }

        return defaultStatus;
    }

    protected Integer emptyResult(R result, Integer emptyStatus) {
        return result == null ? emptyStatus : null;
    }

    /**
     * Calculate resulting content-type
     */
    protected ContentType contentType(R result, O options, ContentType defaultContentType) {
        if (result.contentType() != null) {
            return result.contentType();
        }

        if (options.contentType() != null) {
            return options.contentType();
        }
        return defaultContentType;
    }

    @Override
    public void write(R result, O options) {

        var httpResponse = this.httpResponse.get();

        if (httpResponse.isCommitted()) {
            throw new HttpTeleException("HTTP Response is committed while writing response");
        }

        if (result instanceof HttpResult htr) {
            if (!htr.headers().isEmpty()) {
                HttpUtils.setHeaders(httpResponse, htr.headers());
            }

            if (!htr.cookies().isEmpty()) {
                HttpUtils.setCookies(httpResponse, htr.cookies());
            }
        }

        var emptyStatus = emptyResult(result, 204);
        if (emptyStatus != null) {
            httpResponse.setStatus(emptyStatus).close();
            return;
        }

        var status = status(result, options, 200);
        if (status == null) {
            throw new HttpTeleException("Undefined http status");
        }

        var contentType = contentType(result, options, ContentType.TEXT_PLAIN);
        if (contentType == null) {
            throw new HttpTeleException("Undefined content-type");
        }

        httpResponse.setStatus(status).setContentType(contentType.headerValue());

        // Write response to intermediate buffer first; if successful, copy to output stream.
        // This keeps output stream open for error handling in caller.
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            write(buffer, result, options);
            var outputStream = httpResponse.outputStream();
            buffer.writeTo(outputStream);
            // Do not close outputStream here, will be closed in http server handler
            outputStream.flush();
        } catch (Exception e) {
            throw new HttpTeleException(e);
        }
    }

}
