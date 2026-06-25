package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleWriter;

/**
 * Basic writer for interaction over http
 */
@FunctionalInterface
public interface TeleHttpWriter<V, O extends TeleHttpWriteOptions> extends TeleWriter<V, O> {

}
