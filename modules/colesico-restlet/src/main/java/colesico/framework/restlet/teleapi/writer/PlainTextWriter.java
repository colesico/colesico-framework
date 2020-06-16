package colesico.framework.restlet.teleapi.writer;

import colesico.framework.restlet.teleapi.RestletTWContext;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Proxy for use PlainTextWriter as custom restlet writer
 */
@Singleton
public final class PlainTextWriter extends RestletTeleWriter<Object> {

    private final colesico.framework.telehttp.writer.PlainTextWriter<RestletTWContext> writer;

    @Inject
    public PlainTextWriter(colesico.framework.telehttp.writer.PlainTextWriter writer) {
        super(writer);
        this.writer = writer;
    }

    @Override
    public void write(Object value, RestletTWContext context) {
        writer.write(value, context);
    }
}
