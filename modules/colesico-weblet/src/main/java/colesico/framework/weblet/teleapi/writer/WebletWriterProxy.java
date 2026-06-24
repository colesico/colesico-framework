package colesico.framework.weblet.teleapi.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import colesico.framework.weblet.teleapi.WebletTeleWriter;

public final class WebletWriterProxy<V> implements WebletTeleWriter<V> {

    private final TeleHttpWriter<V, HttpWriteOptions> writer;

    public WebletWriterProxy(TeleHttpWriter<V, HttpWriteOptions> writer) {
        this.writer = writer;
    }

    @Override
    public void write(V value, WebletWriteOptions options) {
        writer.write(value, options);
    }

    public static <V> WebletWriterProxy<V> of(TeleHttpWriter<V, HttpWriteOptions> writer) {
        return new WebletWriterProxy<>(writer);
    }

}
