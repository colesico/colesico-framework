package colesico.framework.weblet.writer;

import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.weblet.WebletWriteOptions;
import colesico.framework.weblet.WebletTeleWriter;

public final class WebletWriterProxy<V> implements WebletTeleWriter<V> {

    private final TeleHttpWriter<V, TeleHttpWriteOptions> writer;

    public WebletWriterProxy(TeleHttpWriter<V, TeleHttpWriteOptions> writer) {
        this.writer = writer;
    }

    @Override
    public void write(V value, WebletWriteOptions options) {
        writer.write(value, options);
    }

    public static <V> WebletWriterProxy<V> of(TeleHttpWriter<V, TeleHttpWriteOptions> writer) {
        return new WebletWriterProxy<>(writer);
    }

}
