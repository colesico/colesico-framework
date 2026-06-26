package colesico.framework.telehttp.writer;

import colesico.framework.ioc.production.Supplier;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Singleton;

/**
 * Object simple writer.
 * Converts object to string with {@link Object#toString()} and
 * writes value with  {@link ToStringWriter}
 */
@Singleton
public class ObjectWriter implements TeleHttpWriter<Object, TeleHttpWriteOptions> {

    protected final SerializingWriter writer;

    public ObjectWriter(Supplier<SerializingWriter> writerSupplier) {
        this.writer = writerSupplier.get();
    }

    @Override
    public void write(Object value, TeleHttpWriteOptions options) {
        writer.write(ValueResponse.of(value), options);
    }

}
