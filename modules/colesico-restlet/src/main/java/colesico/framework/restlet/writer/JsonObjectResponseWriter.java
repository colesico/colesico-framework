package colesico.framework.restlet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.JsonSerializer;
import colesico.framework.restlet.RestletTeleWriter;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.response.ObjectResponse;
import colesico.framework.telehttp.writer.ValueResponseWriter;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Default restlet object response writer.
 * Serialize object value with {@link JsonSerializer}
 */
@Singleton
public class JsonObjectResponseWriter
        extends ValueResponseWriter<ObjectResponse, RestletWriteOptions>
        implements RestletTeleWriter<ObjectResponse> {

    protected final JsonSerializer serializer;

    @Inject
    public JsonObjectResponseWriter(Provider<HttpResponse> httpResponse, JsonSerializer serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.APPLICATION_JSON;
    }

    @Override
    protected void write(OutputStream outputStream, ObjectResponse response, RestletWriteOptions options) throws IOException {
        var contentType = contentType(response, options);
        serializer.serialize(response.value(),
                options.baseType(),
                contentType.charset().orElse(StandardCharsets.UTF_8),
                outputStream);
    }

}
