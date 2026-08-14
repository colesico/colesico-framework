package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.ObjectResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Object response writer based on {@link String#toString()} serialization
 */
public class ToStringObjectResponseWriter<V extends ObjectResponse, O extends HttpWriteOptions> extends ValueResponseWriter<V, O> {

    public ToStringObjectResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    protected void write(OutputStream outputStream, V response, O options) throws IOException {
        var contentType = contentType(response, options);
        if (response.value() != null) {
            outputStream.write(response.value().toString().getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
        }
    }
}
