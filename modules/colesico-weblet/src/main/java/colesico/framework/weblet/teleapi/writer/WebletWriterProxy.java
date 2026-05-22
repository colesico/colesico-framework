package colesico.framework.weblet.teleapi.writer;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import colesico.framework.weblet.teleapi.WebletTeleWriter;

public final class WebletWriterProxy<V> implements WebletTeleWriter<V> {

    private final HttpTeleWriter<V, HttpWriteOptions> writer;

    public WebletWriterProxy(HttpTeleWriter<V, HttpWriteOptions> writer) {
        this.writer = writer;
    }

    @Override
    public void write(V value, Class<V> valueType, WebletWriteOptions options, Channel channel) {
        writer.write(value, valueType, options, channel);
    }

    public static <V> WebletWriterProxy<V> of(HttpTeleWriter<V, HttpWriteOptions> writer) {
        return new WebletWriterProxy<>(writer);
    }

}
