package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.result.ValueResult;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ToStringValueResultWriter
        extends ValueResultWriter<ValueResult<?>, HttpWriteOptions> {

    public ToStringValueResultWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected void write(OutputStream outputStream, ValueResult<?> result, HttpWriteOptions options) throws IOException {
        var contentType = contentType(result, options, ContentType.TEXT_PLAIN);
        outputStream.write(result.value().toString().getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
    }

}
