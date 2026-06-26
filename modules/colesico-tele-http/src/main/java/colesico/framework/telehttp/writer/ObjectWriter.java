package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.StringResponse;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Object simple writer.
 * Converts object to string with {@link Object#toString()} and
 * writes value with  {@link StringResponseWriter}
 */
@Singleton
public class ObjectWriter implements TeleHttpWriter<Object, TeleHttpWriteOptions> {

    protected final Provider<StringResponseWriter> writer;

    public ObjectWriter(Provider<StringResponseWriter> writer) {
        this.writer = writer;
    }

    protected String objectToString(Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    @Override
    public void write(Object value, TeleHttpWriteOptions options) {
        writer.get().write(StringResponse.of(objectToString(value)), options);
    }

}
