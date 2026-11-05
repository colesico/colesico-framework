package colesico.framework.restlet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.JsonConverter;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Default restlet value result writer.
 * Serialize object value with {@link JsonConverter}
 */
@Singleton
public class JsonValueResultWriter
        extends ValueResultWriter<ValueHttpResult<?>, RestletWriteOptions>
        implements RestletWriter<ValueHttpResult<?>> {

    protected final JsonConverter serializer;

    @Inject
    public JsonValueResultWriter(Provider<HttpResponse> httpResponse, JsonConverter serializer) {
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

        Type targetType = options.baseType();

        if (targetType instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?> rawClass && ValueHttpResult.class.isAssignableFrom(rawClass)) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 0) {
                    targetType = typeArguments[0];
                }
            }
        }

        serializer.serialize(result.value(),
                targetType,
                contentType.charset().orElse(StandardCharsets.UTF_8),
                outputStream);
    }

}
