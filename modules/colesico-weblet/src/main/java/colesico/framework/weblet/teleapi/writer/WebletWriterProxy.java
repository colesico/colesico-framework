package colesico.framework.weblet.teleapi.writer;

import colesico.framework.telehttp.HttpTWContext;
import colesico.framework.telehttp.AbstractHttpTeleWriter;
import colesico.framework.weblet.teleapi.WebletTWContext;
import colesico.framework.weblet.teleapi.WebletTeleWriter;

public final class WebletWriterProxy<V> extends WebletTeleWriter<V> {

    private final AbstractHttpTeleWriter<V, HttpTWContext> writer;

    private WebletWriterProxy(AbstractHttpTeleWriter<V, HttpTWContext> writer) {
        super(writer);
        this.writer = writer;
    }

    @Override
    public void write(V value, WebletTWContext context) {
        writer.write(value, context);
    }

    public static <V> WebletWriterProxy of(AbstractHttpTeleWriter<V, HttpTWContext> writer) {
        return new WebletWriterProxy(writer);
    }
}
