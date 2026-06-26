package colesico.framework.telehttp.writer;

import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Singleton;

/**
 * Value simple writer.
 * Writes value with  {@link ValueResponseWriter}
 */
@Unscoped
public class ValueWriter<V> implements TeleHttpWriter<V, TeleHttpWriteOptions> {

    protected final ValueResponseWriter<ValueResponse<V>, TeleHttpWriteOptions> writer;

    public ValueWriter(Supplier<ValueResponseWriter> writerSupplier,
                       @IocMessage TeleHttpResponseWriter.WriterOptions writerOptions) {

        this.writer = writerSupplier.get(writerOptions);
    }

    @Override
    public void write(V value, TeleHttpWriteOptions options) {
        writer.write(ValueResponse.of(value), options);
    }

}
