package colesico.framework.restlet.writer;

import colesico.framework.restlet.RestletTeleWriter;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.telehttp.response.ObjectResponse;
import jakarta.inject.Singleton;

/**
 * Writes object value with  {@link JsonObjectResponseWriter}
 */
@Singleton
public class JsonObjectWriter implements RestletTeleWriter<Object> {

    protected final JsonObjectResponseWriter writer;

    public JsonObjectWriter(JsonObjectResponseWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, RestletWriteOptions options) {
        writer.write(ObjectResponse.value(value).build(), options);
    }
}
