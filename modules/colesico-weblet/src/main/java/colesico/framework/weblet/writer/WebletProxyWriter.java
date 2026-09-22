package colesico.framework.weblet.writer;

import colesico.framework.telehttp.HttpWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.weblet.WebletWriteOptions;
import colesico.framework.weblet.WebletWriter;

public final class WebletProxyWriter<V> implements WebletWriter<V> {

    private final HttpWriter<V, HttpWriteOptions> writer;

    public WebletProxyWriter(HttpWriter<V, HttpWriteOptions> writer) {
        this.writer = writer;
    }

    @Override
    public void write(V value, WebletWriteOptions options) {
        writer.write(value, options);
    }

    public static <V> WebletProxyWriter<V> of(HttpWriter<V, HttpWriteOptions> writer) {
        return new WebletProxyWriter<>(writer);
    }

}
