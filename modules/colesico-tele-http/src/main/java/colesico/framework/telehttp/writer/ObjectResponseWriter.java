package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ObjectResponse;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Simple object writer based on {@link String#toString()} serialization
 */
public class ObjectResponseWriter extends ValueResponseWriter<ObjectResponse, TeleHttpWriteOptions> {

    public ObjectResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    protected Charset charset(String charsetName) {
        if (charsetName != null) {
            return Charset.forName(charsetName);
        }
        return StandardCharsets.UTF_8;
    }

    @Override
    protected MediaType defaultMediaType() {
        return MediaType.TEXT_PLAIN;
    }

    @Override
    protected void write(OutputStream outputStream, ObjectResponse response, TeleHttpWriteOptions options) throws IOException {
        var mediaType = mediaType(response, options);
        outputStream.write(response.value().toString().getBytes(charset(mediaType.charset())));
    }
}
