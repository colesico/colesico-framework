package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.result.StringResult;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StringResultWriter<V extends StringResult, O extends HttpWriteOptions>
        extends AbstractHttpWriter<V, O> {

    public StringResultWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected void write(OutputStream outputStream, V result, O options) throws IOException {
        var contentType = contentType(result, options, ContentType.TEXT_PLAIN);
        outputStream.write(result.value().getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
    }
}
