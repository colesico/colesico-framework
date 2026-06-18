package colesico.framework.restlet.teleapi.writer;

import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.RestletWriteOptions;

/**
 * Default general purpose writer.
 */
public interface RestletResponseWriter extends RestletTeleWriter<Object> {
     void write(Object value, Class<Object> valueType, RestletWriteOptions options);
}
