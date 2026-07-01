package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.BytesResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;

public class BytesResponseWriter extends ValueResponseWriter<BytesResponse, TeleHttpWriteOptions> {

    public BytesResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected MediaType defaultMediaType() {
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    @Override
    protected void write(OutputStream outputStream, BytesResponse response, TeleHttpWriteOptions options) throws IOException {
        outputStream.write(response.value());
    }
}