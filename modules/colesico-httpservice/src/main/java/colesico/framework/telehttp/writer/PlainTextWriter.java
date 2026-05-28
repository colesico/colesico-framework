package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpTeleWriter;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public final class PlainTextWriter implements HttpTeleWriter<Object, HttpWriteOptions> {

    private static final String CONTENT_TYPE = "text/plain";

    private final Provider<HttpResponse> httpResponse;

    @Inject
    public PlainTextWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(Object value, Class<Object> valueType, HttpWriteOptions options) {
        if (value == null) {
            httpResponse.get().sendText("", CONTENT_TYPE, 204);
        } else {
            httpResponse.get().sendText(String.valueOf(value), CONTENT_TYPE, 200);
        }
    }

}
