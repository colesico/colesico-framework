package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ExceptionResponse;
import jakarta.inject.Singleton;

/**
 * General exception writer
 */
@Singleton
public class ExceptionWriter implements TeleHttpWriter<Exception, TeleHttpWriteOptions> {

    protected final ExceptionResponseWriter writer;

    public ExceptionWriter(ExceptionResponseWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Exception value, TeleHttpWriteOptions options) {
        writer.write(ExceptionResponse.exception(value).build(), options);
    }

}
