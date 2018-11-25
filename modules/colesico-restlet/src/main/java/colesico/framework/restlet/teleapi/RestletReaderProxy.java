package colesico.framework.restlet.teleapi;

import colesico.framework.teleapi.TeleReader;

public final class RestletReaderProxy<V, C> implements RestletTeleReader<V, C> {

    private final TeleReader<V, C> reader;

    public RestletReaderProxy(TeleReader<V, C> reader) {
        this.reader = reader;
    }

    @Override
    public V read(C context) {
        return reader.read(context);
    }
}
