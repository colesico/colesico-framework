package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletSerializer;
import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;

@Singleton
public class RestletExceptionWriter implements RestletTeleWriter<RestletException> {

    private final Provider<HttpResponse> httpResponse;
    protected final Supplier<RestletSerializer> serializer;

    public RestletExceptionWriter(Provider<HttpResponse> httpResponse, Supplier<RestletSerializer> serializer) {
        this.httpResponse = httpResponse;
        this.serializer = serializer;
    }

    @Override
    public void write(RestletException value, Class<RestletException> baseType, RestletWriteOptions options) {
        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(500)
                    .setContentType(RestletWriteOptions.DEFAULT_CONTENT_TYPE)
                    .sendText("Unexpected error");
            return;
        }

        var statusCode = value.statusCode();
        if (statusCode == null) {
            statusCode = options.statusCode();
            if (statusCode == null) {
                statusCode = RestletWriteOptions.DEFAULT_ERROR_STATUS_CODE;
            }
        }
        response.setStatus(statusCode);

        var contentType = options.contentType();
        if (contentType == null) {
            contentType = RestletWriteOptions.DEFAULT_CONTENT_TYPE;
        }
        response.setContentType(contentType);

        var charset = options.charset();
        if (charset == null) {
            charset = RestletWriteOptions.DEFAULT_CHARSET;
        }

        String content = serializer.get(contentType).serialize(value.details());
        response.sendData(ByteBuffer.wrap(content.getBytes(charset)));
    }
}
