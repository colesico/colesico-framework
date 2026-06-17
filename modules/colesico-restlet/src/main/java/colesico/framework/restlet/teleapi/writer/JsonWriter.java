package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.RestletWriteOptions;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Singleton
public final class JsonWriter implements ObjectWriter {

    public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";

    protected final Provider<HttpContext> httpContext;
    protected final RestletJsonConverter jsonConverter;

    @Inject
    public JsonWriter(Provider<HttpContext> httpContext, RestletJsonConverter jsonConverter) {
        this.httpContext = httpContext;
        this.jsonConverter = jsonConverter;
    }


    @Override
    public void write(Object value, Class<Object> valueType, RestletWriteOptions options) {
        var response = httpContext.get().response();

        if (value == null) {
            response.setStatus(204)
                    .setContentType(JSON_CONTENT_TYPE)
                    .sendText("");
            return;
        }

        String json = jsonConverter.toJson(value);
        response.setContentType(JSON_CONTENT_TYPE)
                .setStatus(200)
                .sendData(ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8)));
    }
}
