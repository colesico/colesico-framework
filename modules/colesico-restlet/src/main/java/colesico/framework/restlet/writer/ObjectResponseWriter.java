package colesico.framework.restlet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.RestletSerializer;
import colesico.framework.restlet.RestletTeleWriter;
import colesico.framework.restlet.RestletWriteOptions;

import colesico.framework.restlet.response.ObjectResponse;
import colesico.framework.telehttp.MediaType;
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
    public static final MediaType DEFAULT_MEDIA_TYPE = MediaType.ofCharset(MediaType.APPLICATION_JSON, "utf-8");

    protected final Supplier<RestletSerializer> serializer;

    @Inject
    public ObjectResponseWriter(Provider<HttpResponse> httpResponse, Supplier<RestletSerializer> serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected Integer statusCode(ObjectResponse response, RestletWriteOptions options, Integer defaultValue) {

        if (response != null) {
            if (response.content() instanceof Throwable) {
                defaultValue = DEFAULT_ERROR_STATUS_CODE;
            } else {
                defaultValue = DEFAULT_SUCCESS_STATUS_CODE;
            }
        } else {
            defaultValue = DEFAULT_SUCCESS_STATUS_CODE;
        }

        return super.statusCode(response, options, defaultValue);
    }

    @Override
    protected MediaType mediaType(ObjectResponse response, RestletWriteOptions options, MediaType defaultValue) {
        return super.mediaType(response, options, DEFAULT_MEDIA_TYPE);
    }


    @Override
    protected void writeResponse(HttpResponse protocol,
                                 ObjectResponse response,
                                 RestletWriteOptions options,
                                 Integer statusCode,
                                 MediaType mediaType) {


        ByteBuffer content = serializer.get(mediaType.mimeType()).serialize(response.content(), mediaType);
        protocol.sendData(content);

    }
}
