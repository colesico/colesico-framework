package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpTeleWriter;

import colesico.framework.telehttp.response.ContentResponse;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Object simple writer.
 * Writes object use {@link Object#toString()} as plain text
 */
@Singleton
public final class ObjectWriter implements HttpTeleWriter<Object, HttpWriteOptions> {

    private final Provider<TeleHttpResponseWriter> writer;

    public ObjectWriter(Provider<TeleHttpResponseWriter> writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, Class<Object> valueType, HttpWriteOptions options) {
        writer.get().write(ContentResponse.of(value), valueType, options);
    }

}
