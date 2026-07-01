package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.ObjectResponse;
import jakarta.inject.Singleton;

/**
 * Default object writer
 * Writes value with  {@link ObjectResponseWriter}
 */
@Singleton
public class ObjectWriter implements TeleHttpWriter<Object, TeleHttpWriteOptions> {

    protected final ObjectResponseWriter writer;

    public ObjectWriter(ObjectResponseWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, TeleHttpWriteOptions options) {
        writer.write(ObjectResponse.of(value), options);
    }
}
