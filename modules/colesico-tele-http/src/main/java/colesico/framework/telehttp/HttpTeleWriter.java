package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleWriter;

/**
 * Basic writer for interaction over http
 */
@FunctionalInterface
public interface HttpTeleWriter<V, O extends HttpWriteOptions> extends TeleWriter<V, O> {

}
