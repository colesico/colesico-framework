package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;

import colesico.framework.telehttp.response.StringResponse;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Object simple writer.
 * Converts object to string and
 * writes response with  {@link StringResponseWriter}
 */
@Singleton
public class StringifyWriter implements TeleHttpWriter<Object, HttpWriteOptions> {

    protected final Provider<StringResponseWriter> writer;

    public StringifyWriter(Provider<StringResponseWriter> writer) {
        this.writer = writer;
    }

    protected String stringify(Object value) {
        return String.valueOf(value);
    }

    @Override
    public void write(Object value, HttpWriteOptions options) {
        writer.get().write(StringResponse.of(stringify(value)), options);
    }

}
