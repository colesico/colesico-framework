package colesico.framework.restlet.writer;

import colesico.framework.restlet.RestletWriter;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.telehttp.result.ValueHttpResult;
import colesico.framework.telehttp.result.ValueResult;
import jakarta.inject.Singleton;

/**
 * Writes object value with  {@link JsonValueResultWriter}
 */
@Singleton
public class JsonObjectWriter implements RestletWriter<Object> {

    protected final JsonValueResultWriter writer;

    public JsonObjectWriter(JsonValueResultWriter writer) {
        this.writer = writer;
    }

    /**
     * Override this method to provide custom configured builder
     */
    protected ValueResult.Builder resultBuilder(Object value) {
        return ValueResult.value(value);
    }

    @Override
    public void write(Object value, RestletWriteOptions options) {
        if (value instanceof ValueHttpResult<?> vhr) {
            writer.write(vhr, options);
        } else {
            writer.write(resultBuilder(value).build(), options);
        }
    }
}
