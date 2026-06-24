package colesico.framework.restlet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.RestletSerializer;
import colesico.framework.restlet.RestletTeleWriter;
import colesico.framework.restlet.RestletWriteOptions;

import colesico.framework.restlet.response.ObjectResponse;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Singleton
public class ObjectResponseWriter
        extends TeleHttpResponseWriter<ObjectResponse, RestletWriteOptions>
        implements RestletTeleWriter<ObjectResponse> {

    public static final Integer DEFAULT_SUCCESS_STATUS_CODE = 200;
    public static final Integer DEFAULT_ERROR_STATUS_CODE = 500;
    public static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected final Supplier<RestletSerializer> serializer;

    @Inject
    public ObjectResponseWriter(Provider<HttpResponse> httpResponse, Supplier<RestletSerializer> serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected Integer statusCode(ObjectResponse value, RestletWriteOptions options) {
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
    protected String contentType(ObjectResponse value, RestletWriteOptions options) {
        return RestletWriteOptions.DEFAULT_CONTENT_TYPE;
    }

    @Override
    protected void writeResponse(HttpResponse protocol, ObjectResponse response, RestletWriteOptions options, Integer statusCode, String contentType) {

        String content = serializer.get(contentType).serialize(response.content());

        var charset = response.charset();
        if (charset == null) {
            charset = options.charset();
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
        }

        protocol.sendData(ByteBuffer.wrap(content.getBytes(charset)));

    }
}
