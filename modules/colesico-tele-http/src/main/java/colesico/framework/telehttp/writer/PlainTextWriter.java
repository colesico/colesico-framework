package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpTeleWriter;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Object simple writer.
 * Writes object use {@link Object#toString()} as plain text
 */
@Singleton
public final class PlainTextWriter implements HttpTeleWriter<Object, HttpWriteOptions> {

    private static final String CONTENT_TYPE = "text/plain; charset=utf-8";

    private final Provider<HttpResponse> httpResponse;

    @Inject
    public PlainTextWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(Object value, Class<Object> valueType, HttpWriteOptions options) {
        var resp = httpResponse.get();
        resp.setContentType(CONTENT_TYPE);
        if (value == null) {
            resp.setStatus(204);
            resp.sendText("");
        } else {
            resp.setStatus(200);
            resp.sendText(String.valueOf(value));
        }
    }

}
