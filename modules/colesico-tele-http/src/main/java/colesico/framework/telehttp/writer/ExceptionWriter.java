package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.security.authentication.UnauthenticatedException;
import colesico.framework.security.authorization.UnauthorizedException;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class ExceptionWriter implements HttpTeleWriter<Exception, HttpWriteOptions> {

    private final Provider<HttpResponse> httpResponse;

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(Exception exception, Class<Exception> valueType, HttpWriteOptions options) {

        if (exception == null) {
            httpResponse.get().setStatus(500).sendText("Unexpected error");
            return;
        }

        switch (exception) {
            case UnauthenticatedException e -> httpResponse.get()
                    .setContentType("text/plain")
                    .setStatus(401)
                    .sendText("Unauthenticated");
            case UnauthorizedException e -> httpResponse.get()
                    .setContentType("text/plain")
                    .setStatus(401)
                    .sendText("Unauthorized");
            default -> {
                var statusCode = options.statusCode();
                if (statusCode == null) {
                    statusCode = 500;
                }
                httpResponse.get()
                        .setContentType("text/plain")
                        .setStatus(statusCode)
                        .sendText("Server error");
            }
        }
    }

}
