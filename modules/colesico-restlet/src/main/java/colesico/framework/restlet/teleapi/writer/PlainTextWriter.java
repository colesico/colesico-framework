package colesico.framework.restlet.teleapi.writer;

import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import colesico.framework.telehttp.writer.ObjectWriter;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Proxy for use {@link ObjectWriter} as custom restlet writer
 */
@Singleton
public final class PlainTextWriter implements RestletTeleWriter<Object> {

    private final ObjectWriter writer;

    @Inject
    public PlainTextWriter(ObjectWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(Object value, Class<Object> valueType, RestletWriteOptions options) {
        writer.write(value, valueType, options);
    }

}
