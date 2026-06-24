package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.teleapi.RestletSerializer;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.RestletWriteOptions;

import colesico.framework.restlet.teleapi.response.ObjectResponse;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Singleton
public class ObjectResponseWriter
        extends TeleHttpResponseWriter<ObjectResponse, RestletWriteOptions>
        implements RestletTeleWriter<ObjectResponse> {


    protected final Supplier<RestletSerializer> serializer;

    @Inject
    public ObjectResponseWriter(Provider<HttpResponse> httpResponse, Supplier<RestletSerializer> serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected Integer defaultStatusCode(ObjectResponse value, Class<ObjectResponse> valueType, RestletWriteOptions options) {
        if (value != null) {
            if (value.content() instanceof Throwable) {
                return RestletWriteOptions.DEFAULT_ERROR_STATUS_CODE;
            } else {
                return RestletWriteOptions.DEFAULT_SUCCESS_STATUS_CODE;
            }
        }
        return RestletWriteOptions.DEFAULT_SUCCESS_STATUS_CODE;
    }

    @Override
    protected String defaultContentType(ObjectResponse value, Class<ObjectResponse> baseType, RestletWriteOptions options) {
        return RestletWriteOptions.DEFAULT_CONTENT_TYPE;
    }

    @Override
    protected void writeValue(HttpResponse response, ObjectResponse value, Class<ObjectResponse> baseType, RestletWriteOptions options, Integer statusCode, String contentType) {

        String content = serializer.get(contentType).serialize(value.content());

        var charset = value.charset();
        if (charset == null) {
            charset = options.charset();
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
        }

        response.sendData(ByteBuffer.wrap(content.getBytes(charset)));

    }
}
