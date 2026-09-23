package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.result.BytesResult;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;

public class BytesResultWriter<V extends BytesResult, O extends HttpWriteOptions>
        extends ValueHttpResultWriter<V, O> {

    public BytesResultWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected ContentType contentType(V result, O options, ContentType defaultContentType) {
        return super.contentType(result, options, ContentType.APPLICATION_OCTET_STREAM);
    }

    @Override
    protected void write(OutputStream outputStream, V result, O options) throws IOException {
        outputStream.write(result.value());
    }
}