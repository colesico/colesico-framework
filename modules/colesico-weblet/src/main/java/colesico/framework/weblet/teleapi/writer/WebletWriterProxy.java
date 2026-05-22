package colesico.framework.weblet.teleapi.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import colesico.framework.weblet.teleapi.WebletTeleWriter;

public final class WebletWriterProxy<V> extends WebletTeleWriter<V> {

    private final HttpTeleWriter<V, HttpWriteOptions<?,?>> writer;

    private WebletWriterProxy(HttpTeleWriter<V, HttpWriteOptions<?,?>> writer) {
        super(writer);
        this.writer = writer;
    }

    @Override
    public void write(V value, WebletWriteOptions context) {
        writer.write(value, context);
    }

    public static <V> WebletWriterProxy<V> of(HttpTeleWriter<V, HttpWriteOptions<?,?>> writer) {
        return new WebletWriterProxy<>(writer);
    }
}
