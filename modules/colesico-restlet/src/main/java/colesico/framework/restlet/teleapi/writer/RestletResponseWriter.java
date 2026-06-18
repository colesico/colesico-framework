package colesico.framework.restlet.teleapi.writer;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.teleapi.RestletSerializer;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.RestletWriteOptions;

import colesico.framework.restlet.teleapi.response.RestletResponse;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Singleton
public final class RestletResponseWriter
        extends TeleHttpResponseWriter<RestletResponse<?>, RestletWriteOptions>
        implements RestletTeleWriter<RestletResponse<?>> {

    public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";

    private final RestletSerializer serializer;

    @Inject
    public RestletResponseWriter(Provider<HttpResponse> httpResponse, RestletSerializer serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    public void write(RestletResponse<?> value, Class<RestletResponse<?>> valueType, RestletWriteOptions options) {

        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(204)
                    .setContentType(JSON_CONTENT_TYPE)
                    .sendText("");
            return;
        }

        // write if specified headers, cookies
        super.write(value, valueType, options);

        if (StringUtils.isBlank(value.contentType())) {
            response.setContentType(JSON_CONTENT_TYPE);
        }

        if (value.statusCode() == 0) {
            response.setStatus(200);
        }

        String content = serializer.serialize(value);
        response.sendData(ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8)));
    }
}
