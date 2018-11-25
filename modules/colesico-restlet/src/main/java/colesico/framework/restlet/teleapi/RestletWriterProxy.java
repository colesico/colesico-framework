package colesico.framework.restlet.teleapi;

import colesico.framework.teleapi.TeleWriter;

public final class RestletWriterProxy<V, C> implements RestletTeleWriter<V, C> {

    private final TeleWriter<V, C> writer;

    public RestletWriterProxy(TeleWriter<V, C> writer) {
        this.writer = writer;
    }

    @Override
    public void write(V value, C context) {
        writer.write(value, context);
    }
}
