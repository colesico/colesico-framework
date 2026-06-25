package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleReader;

/**
 * Basic reader for retrieving param vales from http request
 */
@FunctionalInterface
public interface TeleHttpReader<V, O extends TeleHttpReadOptions> extends TeleReader<V, O> {

}
