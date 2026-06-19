package colesico.framework.restlet.teleapi.writer;

import colesico.framework.assist.StringUtils;
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
    public void write(ObjectResponse value, Class<ObjectResponse> valueType, RestletWriteOptions options) {

        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(204)
                    .setContentType(RestletWriteOptions.DEFAULT_CONTENT_TYPE)
                    .sendText("");
            return;
        }

        // write if specified headers, cookies
        super.write(value, valueType, options);

        var contentType = value.contentType();
        if (contentType == null) {
            contentType = options.contentType();
            response.setContentType(contentType);
        }

        if (value.statusCode() == null) {
            response.setStatus(options.statusCode());
        }

        String content = serializer.get(contentType).serialize(value.content());

        var charset = value.charset();
        if (charset == null) {
            charset = options.charset();
        }
        response.sendData(ByteBuffer.wrap(content.getBytes(charset)));
    }
}
