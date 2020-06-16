package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.HttpTWContext;
import colesico.framework.telehttp.HttpTeleWriter;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public final class PlainTextWriter<C extends HttpTWContext> extends HttpTeleWriter<Object, C> {

    private static final String CONTENT_TYPE = "text/plain; charset=utf-8";

    @Inject
    public PlainTextWriter(Provider<HttpContext> httpContextProv) {
        super(httpContextProv);
    }

    @Override
    public void write(Object value, C context) {
        if (value == null) {
            getResponse().sendText("", CONTENT_TYPE, 204);
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

        getResponse().sendText(str, CONTENT_TYPE, 200);
    }
}
