package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ObjectResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Simple object writer based on {@link String#toString()} serialization
 */
public class ObjectResponseWriter extends ValueResponseWriter<ObjectResponse, TeleHttpWriteOptions> {

    public ObjectResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    protected void write(OutputStream outputStream, ObjectResponse response, TeleHttpWriteOptions options) throws IOException {
        var contentType = contentType(response, options);
        outputStream.write(response.value().toString().getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
    }
}
