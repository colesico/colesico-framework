package colesico.framework.weblet.reader;

import colesico.framework.telehttp.TeleHttpReadOptions;
import colesico.framework.telehttp.TeleHttpReader;
import colesico.framework.weblet.WebletReadOptions;
import colesico.framework.weblet.WebletTeleReader;

public final class WebletReaderProxy<V> implements WebletTeleReader<V> {

    private final TeleHttpReader<V, TeleHttpReadOptions> reader;

    private WebletReaderProxy(TeleHttpReader<V, TeleHttpReadOptions> reader) {
        this.reader = reader;
    }

    public static <V> WebletReaderProxy<V> of(TeleHttpReader<V, TeleHttpReadOptions> reader) {
        return new WebletReaderProxy<>(reader);
    }

    @Override
    public V read(WebletReadOptions options) {
        return reader.read(options);
    }
}
