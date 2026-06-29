package colesico.framework.telehttp.writer;

import colesico.framework.config.Config;
import colesico.framework.ioc.production.Classed;
import colesico.framework.security.authentication.UnauthenticatedException;
import colesico.framework.security.authorization.UnauthorizedException;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Singleton;

/**
 * General exception writer
 */
@Singleton
public class ExceptionWriter implements TeleHttpWriter<Exception, TeleHttpWriteOptions> {

    protected final ValueResponseWriter<ValueResponse<String>, TeleHttpWriteOptions> writer;

    public ExceptionWriter(@Classed(WriterConfig.class) ValueResponseWriter writer) {
        this.writer = writer;
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
            writer.write(ValueResponse.of(500, "Unknown error"), options);
            return;
        }

        switch (exception) {
            case UnauthenticatedException e -> writer.write(ValueResponse.of(401, "Unauthenticated"), options);
            case UnauthorizedException e -> writer.write(ValueResponse.of(401, "Unauthorized"), options);
            case TeleHttpException e -> {
                var status = statusCode(e, options, 500);
                writer.write(ValueResponse.of(status, String.valueOf(e.details())), options);
            }
            default -> {
                var status = statusCode(exception, options, 500);
                writer.write(ValueResponse.of(status, "Server error"), options);
            }
        }
    }

    @Config
    public static class WriterConfig extends ValueResponseWriter.Config {

        @Override
        public MediaType defaultMediaType() {
            return MediaType.TEXT_PLAIN;
        }
    }
}
