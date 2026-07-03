package colesico.framework.weblet.reader;

import colesico.framework.telehttp.HttpReader;
import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.weblet.WebletReadOptions;
import colesico.framework.weblet.WebletReader;

public final class WebletReaderProxy<V> implements WebletReader<V> {

    private final HttpReader<V, HttpReadOptions> reader;

    private WebletReaderProxy(HttpReader<V, HttpReadOptions> reader) {
        this.reader = reader;
    }

    public static <V> WebletReaderProxy<V> of(HttpReader<V, HttpReadOptions> reader) {
        return new WebletReaderProxy<>(reader);
    }

    @Override
    public V read(WebletReadOptions options) {
        return reader.read(options);
    }
}
