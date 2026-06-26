package colesico.framework.telehttp.writer;

import colesico.framework.ioc.production.Supplier;
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

    protected static final TeleHttpResponseWriter.WriterOptions WRITER_OPTIONS =
            TeleHttpResponseWriter.WriterOptions.of(500, MediaType.ofCharset(TextPlainSerializer.MIME_TYPE, "utf-8"));

    protected final ValueResponseWriter<ValueResponse<String>, TeleHttpWriteOptions> writer;

    public ExceptionWriter(Supplier<ValueResponseWriter> writerSupplier) {
        this.writer = writerSupplier.get(WRITER_OPTIONS);
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
}
