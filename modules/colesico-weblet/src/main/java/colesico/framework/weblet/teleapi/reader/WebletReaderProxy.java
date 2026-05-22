package colesico.framework.weblet.teleapi.reader;

import colesico.framework.telehttp.HttpTeleContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.weblet.teleapi.WebletTeleContext;
import colesico.framework.weblet.teleapi.WebletTeleReader;

public final class WebletReaderProxy<V> extends WebletTeleReader<V> {

    private final HttpTeleReader<V, HttpTeleContext<?,?>> reader;

    private WebletReaderProxy(HttpTeleReader<V, HttpTeleContext<?,?>> reader) {
        this.reader = reader;
    }

    @Override
    public V read(WebletTeleContext context) {
        return reader.read(context);
    }

    public static <V> WebletReaderProxy of(HttpTeleReader<V, HttpTeleContext<?,?>> reader) {
        return new WebletReaderProxy(reader);
    }
}
