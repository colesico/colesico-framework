package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.security.authentication.UnauthenticatedException;
import colesico.framework.security.authorization.UnauthorizedException;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpTeleException;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.ExceptionResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Default exception response writer
 * @param <O>
 */
public class ExceptionResponseWriter<O extends HttpWriteOptions>
        extends ValueResponseWriter<ExceptionResponse, O> {

    public ExceptionResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    protected Integer defaultStatusCode() {
        return 500;
    }

    @Override
    protected Integer emptyStatusCode() {
        return 500;
    }

    @Override
    protected Integer statusCode(ExceptionResponse response, O options) {
        return switch (response.value()) {
            case UnauthenticatedException e -> 401;
            case UnauthorizedException e -> 401;
            case HttpTeleException e -> e.statusCode() != null ? e.statusCode() : 500;
            default -> super.statusCode(response, options);
        };
    }

    protected String errorDetails(Exception exception) {
        return switch (exception) {
            case UnauthenticatedException e -> "Unauthenticated";
            case UnauthorizedException e -> "Unauthorized";
            case HttpTeleException e -> e.details() != null ? e.details().toString() : "Error";
            default -> "Error";
        };
    }

    @Override
    protected void write(OutputStream outputStream, ExceptionResponse response, O options) throws IOException {
        var contentType = contentType(response, options);
        outputStream.write(errorDetails(response.value()).getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
    }
}
