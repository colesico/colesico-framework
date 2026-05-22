package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleWriter;

/**
 * Basic writer for interaction over http
 */
@FunctionalInterface
public interface HttpTeleWriter<V, W extends HttpWriteOptions> extends TeleWriter<V, W> {

}
