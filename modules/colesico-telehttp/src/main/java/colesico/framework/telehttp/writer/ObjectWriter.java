package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.HttpWriteOptions;

import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.result.ValueResult;
import colesico.framework.telehttp.result.ValueHttpResult;
import jakarta.inject.Singleton;

/**
 * Writes object value
 */
@Singleton
public class ObjectWriter
        implements HttpWriter<Object, HttpWriteOptions> {

    private final ValueResultWriter<ValueHttpResult<?>, HttpWriteOptions> writer;

    public ObjectWriter(ValueResultWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, HttpWriteOptions options) {
        if (value instanceof ValueHttpResult<?> vhr) {
            writer.write(vhr, options);
        } else {
            writer.write(ValueResult.value(value).build(), options);
        }
    }
}
