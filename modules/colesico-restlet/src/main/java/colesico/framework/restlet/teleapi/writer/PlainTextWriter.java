package colesico.framework.restlet.teleapi.writer;

import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Proxy for use {@link colesico.framework.telehttp.writer.PlainTextWriter} as custom restlet writer
 */
@Singleton
public final class PlainTextWriter implements RestletTeleWriter<Object> {

    private final colesico.framework.telehttp.writer.PlainTextWriter writer;

    @Inject
    public PlainTextWriter(colesico.framework.telehttp.writer.PlainTextWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, Class<Object> valueType, RestletWriteOptions options) {
        writer.write(value, valueType, options);
    }

}
