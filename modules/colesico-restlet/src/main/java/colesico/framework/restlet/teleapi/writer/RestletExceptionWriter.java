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

import static colesico.framework.restlet.teleapi.response.RestletResponse.DEFAULT_CHARSET;
import static colesico.framework.restlet.teleapi.response.RestletResponse.DEFAULT_CONTENT_TYPE;

@Singleton
public class RestletExceptionWriter implements RestletTeleWriter<RestletException> {

    private final Provider<HttpResponse> httpResponse;
    protected final Supplier<RestletSerializer> serializer;

    public RestletExceptionWriter(Provider<HttpResponse> httpResponse, Supplier<RestletSerializer> serializer) {
        this.httpResponse = httpResponse;
        this.serializer = serializer;
    }

    @Override
    public void write(RestletException value, Class<RestletException> valueType, RestletWriteOptions options) {
        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(500)
                    .setContentType(DEFAULT_CONTENT_TYPE)
                    .sendText("Unexpected error");
            return;
        }

        response.setContentType(DEFAULT_CONTENT_TYPE);

        if (value.statusCode() == null) {
            response.setStatus(500);
        } else {
            response.setStatus(value.statusCode());
        }

        String content = serializer.get(contentType).serialize(value.details());

        var charset = value.charset();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        response.sendData(ByteBuffer.wrap(content.getBytes(charset)));
    }
}
