package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.StringResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StringResponseWriter<V extends StringResponse, O extends HttpWriteOptions> extends ValueResponseWriter<V, O> {

    public StringResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    protected void write(OutputStream outputStream, V response, O options) throws IOException {
        var contentType = contentType(response, options);
        outputStream.write(response.value().getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
    }
}
