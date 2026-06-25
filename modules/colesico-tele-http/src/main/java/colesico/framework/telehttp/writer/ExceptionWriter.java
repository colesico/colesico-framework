package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.security.authentication.UnauthenticatedException;
import colesico.framework.security.authorization.UnauthorizedException;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.StringResponse;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * General exception writer
 */
@Singleton
public class ExceptionWriter implements TeleHttpWriter<Exception, TeleHttpWriteOptions> {

    public static final String DEFAULT_CONTENT_TYPE = "text/plain";

    protected final Provider<StringResponseWriter> writerProvider;

    public ExceptionWriter(Provider<StringResponseWriter> writerProvider) {
        this.writerProvider = writerProvider;
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

        StringResponseWriter writer = writerProvider.get();

        if (exception == null) {
            writer.write(StringResponse.of(500, "Unknown error"), options);
            return;
        }

        switch (exception) {
            case UnauthenticatedException e -> writer.write(StringResponse.of(401, "Unauthenticated"), options);
            case UnauthorizedException e -> writer.write(StringResponse.of(401, "Unauthorized"), options);
            case TeleHttpException e -> {
                var status = statusCode(e, options, 500);
                writer.write(StringResponse.of(status, String.valueOf(e.details())), options);
            }
            default -> {
                var status = statusCode(exception, options, 500);
                writer.write(StringResponse.of(status, "Server error"), options);
            }

        }
    }
}
