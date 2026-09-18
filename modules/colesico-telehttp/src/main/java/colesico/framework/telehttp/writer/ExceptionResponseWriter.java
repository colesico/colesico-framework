package colesico.framework.telehttp.writer;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpTeleError;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.ExceptionResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Default exception response writer
 *
 * @param <O>
 */
public class ExceptionResponseWriter<O extends HttpWriteOptions>
        extends ValueResponseWriter<ExceptionResponse, O> {

    public ExceptionResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected Integer statusCode(ExceptionResponse response, O options, Integer defaultValue) {
        if (response.statusCode() != null) {
            return response.statusCode();
        }

        if (response.value() instanceof HttpTeleError hte) {
            if (hte.statusCode() != null) {
                return hte.statusCode();
            }
        }

        if (options.statusCode() != null) {
            return options.statusCode();
        }

        return 500;
    }

    @Override
    protected Integer emptyStatusCode(ExceptionResponse response, O options) {
        return 500;
    }

    protected String errorData(ExceptionResponse response) {
        var exception = response.value();

        String data = "Exception: " + exception.getClass().getCanonicalName();
        if (!StringUtils.isBlank(exception.getMessage())) {
            data = data + "; message: " + exception.getMessage();
        }

        if (exception instanceof HttpTeleError hte) {
            if (hte.errorCode() != null) {
                data = data + "; errorCode: " + hte.errorCode();
            }

            if (hte.details() != null) {
                data = data + "; details: " + hte.details().toString();
            }

        }

        return data;
    }

    @Override
    protected void write(OutputStream outputStream, ExceptionResponse response, O options) throws IOException {
        var contentType = contentType(response, options, ContentType.TEXT_PLAIN);
        outputStream.write(errorData(response).getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
    }
}
