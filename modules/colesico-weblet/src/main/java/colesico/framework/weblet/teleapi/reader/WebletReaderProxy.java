package colesico.framework.weblet.teleapi.reader;

import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.weblet.teleapi.WebletTRContext;
import colesico.framework.weblet.teleapi.WebletTeleReader;


public final class WebletReaderProxy<V> extends WebletTeleReader<V> {

    private final HttpTeleReader<V, HttpTRContext> reader;

    private WebletReaderProxy(HttpTeleReader<V, HttpTRContext> reader) {
        super(reader);
        this.reader = reader;
    }

    @Override
    public V read(WebletTRContext context) {
        return reader.read(context);
    }

    public static <V> WebletReaderProxy of(HttpTeleReader<V, HttpTRContext> reader) {
        return new WebletReaderProxy(reader);
    }
}
