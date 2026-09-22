package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpWriter;

import colesico.framework.telehttp.result.ObjectResult;
import colesico.framework.telehttp.result.ValueResult;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Writes object value
 */
@Singleton
public class ToStringObjectWriter extends AbstractHttpWriter<Object, HttpWriteOptions> {

    public ToStringObjectWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected void write(OutputStream outputStream, Object result, HttpWriteOptions options) throws IOException {
        var contentType = contentType(result, options, ContentType.TEXT_PLAIN);
        var value = result instanceof ValueResult<?> vr ? vr.value() : result;
        if (value != null) {
            outputStream.write(value.toString().getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
        }
    }

}
