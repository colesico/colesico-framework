package colesico.framework.restlet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.JsonSerializer;
import colesico.framework.restlet.RestletWriter;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.result.ValueHttpResult;
import colesico.framework.telehttp.writer.ValueResultWriter;
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
public class JsonValueHttpResultWriter
        extends ValueResultWriter<ValueHttpResult<?>, RestletWriteOptions>
        implements RestletWriter<ValueHttpResult<?>> {

    protected final JsonSerializer serializer;

    @Inject
    public JsonValueHttpResultWriter(Provider<HttpResponse> httpResponse, JsonSerializer serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected ContentType contentType(ObjectResult result, RestletWriteOptions options, ContentType defaultContentType) {
        return super.contentType(result, options, ContentType.APPLICATION_JSON);
    }

    @Override
    protected void write(OutputStream outputStream, ObjectResult response, RestletWriteOptions options) throws IOException {
        var contentType = contentType(response, options, null);
        serializer.serialize(response.value(),
                options.baseType(),
                contentType.charset().orElse(StandardCharsets.UTF_8),
                outputStream);
    }

}
