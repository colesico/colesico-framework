package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.BytesResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;

public class BytesResponseWriter<V extends BytesResponse, O extends HttpWriteOptions> extends ValueResponseWriter<V, O> {

    public BytesResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.APPLICATION_OCTET_STREAM;
    }

    @Override
    protected void write(OutputStream outputStream, V response, O options) throws IOException {
        outputStream.write(response.value());
    }
}