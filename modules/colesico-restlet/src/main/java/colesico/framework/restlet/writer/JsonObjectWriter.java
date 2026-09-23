package colesico.framework.restlet.writer;

import colesico.framework.restlet.RestletWriter;
import colesico.framework.restlet.RestletWriteOptions;
import jakarta.inject.Singleton;

/**
 * Writes object value with  {@link JsonValueHttpResultWriter}
 */
@Singleton
public class JsonObjectWriter implements RestletWriter<Object> {

    protected final JsonValueHttpResultWriter writer;

    public JsonObjectWriter(JsonValueHttpResultWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, RestletWriteOptions options) {
        writer.write(ObjectResult.value(value).build(), options);
    }
}
