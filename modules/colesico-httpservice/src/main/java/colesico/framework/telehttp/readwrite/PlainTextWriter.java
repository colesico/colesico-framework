package colesico.framework.telehttp.readwrite;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTWContext;
import colesico.framework.telehttp.AbstractHttpTeleWriter;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public final class PlainTextWriter extends AbstractHttpTeleWriter<Object, HttpTWContext<?, ?>> {

    private static final String CONTENT_TYPE = "text/plain; charset=utf-8";

    @Inject
    public PlainTextWriter(Provider<HttpContext> httpContextProv) {
        super(httpContextProv);
    }

    @Override
    public void write(Object value, HttpTWContext<?, ?> context) {
        if (value == null) {
            response().sendText("", CONTENT_TYPE, 204);
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
            str = "" + (Character) value;
        } else if (value instanceof Byte) {
            str = Byte.toString((Byte) value);
        } else {
            str = value.toString();
        }

        response().sendText(str, CONTENT_TYPE, 200);
    }
}
