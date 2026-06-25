package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.security.authentication.UnauthenticatedException;
import colesico.framework.security.authorization.UnauthorizedException;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * General exception writer
 */
@Singleton
public class ExceptionWriter implements TeleHttpWriter<Exception, TeleHttpWriteOptions> {

    public static final String DEFAULT_CONTENT_TYPE = "text/plain";

    private final Provider<HttpResponse> httpResponse;

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    protected Integer statusCode(Exception exception, TeleHttpWriteOptions options, Integer defaultCode) {
        if (exception instanceof TeleHttpException e) {
            if (e.statusCode() != null) {
                return e.statusCode();
            }
        }
        if (options.statusCode() != null) {
            return options.statusCode();
        }
        return defaultCode;
    }

    @Override
    public void write(Exception exception, TeleHttpWriteOptions options) {

        if (exception == null) {
            httpResponse.get()
                    .setStatus(500)
                    .setContentType(DEFAULT_CONTENT_TYPE)
                    .sendText("Unknown error");
            return;
        }

        switch (exception) {
            case UnauthenticatedException e -> httpResponse.get()
                    .setContentType(DEFAULT_CONTENT_TYPE)
                    .setStatus(401)
                    .sendText("Unauthenticated");
            case UnauthorizedException e -> httpResponse.get()
                    .setContentType(DEFAULT_CONTENT_TYPE)
                    .setStatus(401)
                    .sendText("Unauthorized");
            case TeleHttpException e -> httpResponse.get()
                    .setContentType(DEFAULT_CONTENT_TYPE)
                    .setStatus(statusCode(e, options, 500))
                    .sendText(String.valueOf(e.details()));
            default -> httpResponse.get()
                    .setContentType(DEFAULT_CONTENT_TYPE)
                    .setStatus(statusCode(exception, options, 500))
                    .sendText("Server error");

        }
    }
}
