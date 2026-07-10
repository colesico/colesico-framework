package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.ExceptionResponse;
import jakarta.inject.Singleton;

/**
 * Default exception writer
 */
@Singleton
public class ExceptionWriter implements HttpWriter<Exception, HttpWriteOptions> {

    protected final ExceptionResponseWriter writer;

    public ExceptionWriter(ExceptionResponseWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Exception value, HttpWriteOptions options) {
        writer.write(ExceptionResponse.exception(value).build(), options);
    }

}
