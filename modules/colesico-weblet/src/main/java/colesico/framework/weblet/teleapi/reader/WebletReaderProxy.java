package colesico.framework.weblet.teleapi.reader;

import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.weblet.teleapi.WebletReadOptions;
import colesico.framework.weblet.teleapi.WebletTeleReader;

public final class WebletReaderProxy<V> implements WebletTeleReader<V> {

    private final HttpTeleReader<V, HttpReadOptions> reader;

    private WebletReaderProxy(HttpTeleReader<V, HttpReadOptions> reader) {
        this.reader = reader;
    }

    public static <V> WebletReaderProxy<V> of(HttpTeleReader<V, HttpReadOptions> reader) {
        return new WebletReaderProxy<>(reader);
    }

    @Override
    public V read(Class<V> valueType, WebletReadOptions options) {
        return reader.read(valueType, options);
    }
}
