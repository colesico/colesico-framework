package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpTeleWriter;

import colesico.framework.telehttp.response.StringResponse;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Object simple writer.
 * Uses {@link StringResponseWriter} under the hood
 */
@Singleton
public final class ObjectWriter implements HttpTeleWriter<Object, HttpWriteOptions> {

    private final Provider<StringResponseWriter> writer;

    public ObjectWriter(Provider<StringResponseWriter> writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, HttpWriteOptions options) {
        var content = String.valueOf(value);
        writer.get().write(StringResponse.of(content), options);
    }

}
