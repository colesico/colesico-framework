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

        return super.statusCode(response,options,defaultValue);
    }

    @Override
    protected String mediaType(ObjectResponse response, RestletWriteOptions options, String defaultValue) {
        return super.mediaType(response, options, DEFAULT_CONTENT_TYPE);
    }

    protected Charset charset(ObjectResponse response, RestletWriteOptions options, Charset defaultValue) {
        if (response.charset() != null) {
            return response.charset();
        }
        if (options.charset() != null) {
            return options.charset();
        }
        return defaultValue;
    }

    @Override
    protected void writeResponse(HttpResponse protocol, ObjectResponse response, RestletWriteOptions options, Integer statusCode, String contentType) {

        var charset = charset(response, options, DEFAULT_CHARSET);
        ByteBuffer content = serializer.get(contentType).serialize(response.content(), contentType, charset);
        protocol.sendData(content);

    }
}
