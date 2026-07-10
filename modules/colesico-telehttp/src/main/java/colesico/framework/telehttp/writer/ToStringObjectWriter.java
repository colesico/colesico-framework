package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpWriter;

import colesico.framework.telehttp.response.ObjectResponse;
import jakarta.inject.Singleton;

/**
 * Writes object value with  {@link ToStringObjectResponseWriter}
 */
@Singleton
public class ToStringObjectWriter implements HttpWriter<Object, HttpWriteOptions> {

    protected final ToStringObjectResponseWriter writer;

    public ToStringObjectWriter(ToStringObjectResponseWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, HttpWriteOptions options) {
        writer.write(ObjectResponse.value(value).build(), options);
    }
}
