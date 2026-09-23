package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriteOptions;

import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.result.ObjectResult;
import colesico.framework.telehttp.result.ValueResult;
import jakarta.inject.Singleton;

/**
 * Writes object value
 */
@Singleton
public class ObjectWriter
        implements HttpWriter<Object, HttpWriteOptions> {

    private final ValueResultWriter<ValueResult<?>, HttpWriteOptions> writer;

    public ObjectWriter(ValueResultWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, HttpWriteOptions options) {
        if (value instanceof ValueResult<?> vr) {
            writer.write(vr, options);
        } else {
            writer.write(ObjectResult.value(value).build(), options);
        }
    }
}
