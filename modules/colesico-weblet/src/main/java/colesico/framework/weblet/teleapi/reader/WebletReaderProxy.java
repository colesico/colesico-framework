package colesico.framework.weblet.teleapi.reader;

import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.TeleHttpReader;
import colesico.framework.weblet.teleapi.WebletReadOptions;
import colesico.framework.weblet.teleapi.WebletTeleReader;

public final class WebletReaderProxy<V> implements WebletTeleReader<V> {

    private final TeleHttpReader<V, HttpReadOptions> reader;

    private WebletReaderProxy(TeleHttpReader<V, HttpReadOptions> reader) {
        this.reader = reader;
    }

    public static <V> WebletReaderProxy<V> of(TeleHttpReader<V, HttpReadOptions> reader) {
        return new WebletReaderProxy<>(reader);
    }

    @Override
    public V read(Class<V> baseType, WebletReadOptions options) {
        return reader.read(baseType, options);
    }
}
