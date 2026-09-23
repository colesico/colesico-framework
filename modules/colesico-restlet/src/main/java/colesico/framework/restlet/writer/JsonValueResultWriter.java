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
 * Default restlet value result writer.
 * Serialize object value with {@link JsonSerializer}
 */
@Singleton
public class JsonValueResultWriter
        extends ValueResultWriter<ValueHttpResult<?>, RestletWriteOptions>
        implements RestletWriter<ValueHttpResult<?>> {

    protected final JsonSerializer serializer;

    @Inject
    public JsonValueResultWriter(Provider<HttpResponse> httpResponse, JsonSerializer serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected ContentType contentType(ValueHttpResult<?> result, RestletWriteOptions options, ContentType defaultContentType) {
        return super.contentType(result, options, ContentType.APPLICATION_JSON);
    }

    @Override
    protected void write(OutputStream outputStream, ValueHttpResult<?> result, RestletWriteOptions options) throws IOException {
        var contentType = contentType(result, options, null);
        serializer.serialize(result.value(),
                options.baseType(),
                contentType.charset().orElse(StandardCharsets.UTF_8),
                outputStream);
    }

}
