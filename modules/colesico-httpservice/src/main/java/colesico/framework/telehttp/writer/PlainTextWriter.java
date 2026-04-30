package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.HttpTWContext;
import colesico.framework.telehttp.HttpTeleWriter;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public final class PlainTextWriter extends HttpTeleWriter<Object, HttpTWContext<?, ?>> {

    @Inject
    public PlainTextWriter(Provider<HttpContext> httpContextProv) {
        super(httpContextProv);
    }

    @Override
    public void write(Object value, HttpTWContext<?, ?> context) {
        if (value == null) {
            response().sendText("",
                    context.contentType() != null ? context.contentType() : CONTENT_TYPE,
                    context.statusCode() != null ? context.statusCode() : 204);
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

        response().sendText(str,
                context.contentType() != null ? context.contentType() : CONTENT_TYPE,
                context.statusCode() != null ? context.statusCode() : 200);
    }
}
