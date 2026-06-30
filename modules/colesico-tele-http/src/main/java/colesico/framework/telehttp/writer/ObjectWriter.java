package colesico.framework.telehttp.writer;

import colesico.framework.config.Config;
import colesico.framework.config.DefaultMessage;
import colesico.framework.ioc.production.Classed;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Singleton;

/**
 * General Object writer
 * Writes actualResponse with  {@link ValueResponseWriter}
 */
@Singleton
public class ObjectWriter implements TeleHttpWriter<Object, TeleHttpWriteOptions> {

    protected final ValueResponseWriter<ValueResponse<Object>, TeleHttpWriteOptions> writer;

    public ObjectWriter(ValueResponseWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, TeleHttpWriteOptions options) {
        writer.write(ValueResponse.of(value), options);
    }


}
