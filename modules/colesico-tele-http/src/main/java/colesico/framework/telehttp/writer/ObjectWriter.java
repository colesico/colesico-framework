package colesico.framework.telehttp.writer;

import colesico.framework.ioc.production.Supplier;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Singleton;

/**
 * General Object writer
 * Writes value with  {@link ValueResponseWriter}
 */
@Singleton
public class ObjectWriter implements TeleHttpWriter<Object, TeleHttpWriteOptions> {

    protected static final TeleHttpResponseWriter.WriterOptions WRITER_OPTIONS =
            TeleHttpResponseWriter.WriterOptions.of(200, MediaType.ofCharset(TextPlainSerializer.MIME_TYPE, "utf-8"));


    protected final ValueResponseWriter<ValueResponse<Object>, TeleHttpWriteOptions> writer;

    public ObjectWriter(Supplier<ValueResponseWriter> writerSupplier) {
        this.writer = writerSupplier.get(WRITER_OPTIONS);
    }

    @Override
    public void write(Object value, TeleHttpWriteOptions options) {
        writer.write(ValueResponse.of(value), options);
    }

}
