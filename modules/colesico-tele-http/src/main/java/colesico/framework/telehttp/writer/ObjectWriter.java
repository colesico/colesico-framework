package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.StringResponse;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Object simple writer.
 * Uses {@link StringResponseWriter} under the hood
 */
@Singleton
public class ObjectWriter implements TeleHttpWriter<Object, HttpWriteOptions> {

    private final Provider<StringResponseWriter> writer;

    public ObjectWriter(Provider<StringResponseWriter> writer) {
        this.writer = writer;
    }

    protected String asString(Object value) {
        return String.valueOf(value);
    }

    @Override
    public void write(Object value, HttpWriteOptions options) {
        writer.get().write(StringResponse.of(asString(value)), options);
    }

}
