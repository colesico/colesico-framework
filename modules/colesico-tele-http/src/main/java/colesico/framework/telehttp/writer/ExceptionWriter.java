package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.security.authentication.UnauthenticatedException;
import colesico.framework.security.authorization.UnauthorizedException;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * General exception writer
 */
@Singleton
public class ExceptionWriter implements TeleHttpWriter<Exception, HttpWriteOptions> {

    public static final String CONTENT_TYPE = "text/plain";

    private final Provider<HttpResponse> httpResponse;

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(Exception exception, HttpWriteOptions options) {

        if (exception == null) {
            httpResponse.get()
                    .setStatus(500)
                    .setContentType(CONTENT_TYPE)
                    .sendText("Unexpected error");
            return;
        }

        switch (exception) {
            case UnauthenticatedException e -> httpResponse.get()
                    .setContentType(CONTENT_TYPE)
                    .setStatus(401)
                    .sendText("Unauthenticated");
            case UnauthorizedException e -> httpResponse.get()
                    .setContentType(CONTENT_TYPE)
                    .setStatus(401)
                    .sendText("Unauthorized");
            case TeleHttpException e -> {
                var statusCode = e.statusCode();
                if (statusCode == null) {
                    statusCode = options.statusCode();
                    if (statusCode == null) {
                        statusCode = 500;
                    }
                }
                httpResponse.get()
                        .setContentType(CONTENT_TYPE)
                        .setStatus(statusCode)
                        .sendText(String.valueOf(e.details()));
            }
            default -> {
                var statusCode = options.statusCode();
                if (statusCode == null) {
                    statusCode = 500;
                }
                httpResponse.get()
                        .setContentType(CONTENT_TYPE)
                        .setStatus(statusCode)
                        .sendText("Server error");
            }
        }
    }

}
