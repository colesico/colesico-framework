package colesico.framework.restlet.teleapi.writer;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.restlet.teleapi.RestletSerializer;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.RestletWriteOptions;

import colesico.framework.restlet.teleapi.response.RestletResponse;
import colesico.framework.telehttp.response.TeleHttpResponse;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;

import static colesico.framework.restlet.teleapi.response.RestletResponse.DEFAULT_CHARSET;
import static colesico.framework.restlet.teleapi.response.RestletResponse.DEFAULT_CONTENT_TYPE;

@Singleton
public class RestletResponseWriter
        extends TeleHttpResponseWriter<RestletResponse, RestletWriteOptions>
        implements RestletTeleWriter<RestletResponse> {


    protected final Supplier<RestletSerializer> serializer;

    @Inject
    public RestletResponseWriter(Provider<HttpResponse> httpResponse, Supplier<RestletSerializer> serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    public void write(RestletResponse value, Class<RestletResponse> valueType, RestletWriteOptions options) {

        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(204)
                    .setContentType(DEFAULT_CONTENT_TYPE)
                    .sendText("");
            return;
        }

        // write if specified headers, cookies
        super.write(value, valueType, options);

        var contentType = value.contentType();
        if (StringUtils.isBlank(contentType)) {
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
