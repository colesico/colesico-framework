package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpTeleWriter;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public final class PlainTextWriter implements HttpTeleWriter<Object, HttpWriteOptions> {

    private static final String CONTENT_TYPE = "text/plain";

    private final Provider<HttpResponse> httpResponse;

    public PlainTextWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(Object value, Class<Object> valueType, HttpWriteOptions options) {
        if (value == null) {
            httpResponse.get().sendText("", CONTENT_TYPE, 204);
        }

        String str = "";
        if (value instanceof String) {
            str = (String) value;
        } else if (value instanceof Long) {
            str = Long.toString((Long) value);
        } else if (value instanceof Integer) {
            str = Integer.toString((Integer) value);
        } else if (value instanceof Short) {
            str = Short.toString((Short) value);
        } else if (value instanceof Character) {
            str = "" + value;
        } else if (value instanceof Byte) {
            str = Byte.toString((Byte) value);
        } else {
            str = value.toString();
        }

        httpResponse.get().sendText(str, CONTENT_TYPE, 200);
    }

}
