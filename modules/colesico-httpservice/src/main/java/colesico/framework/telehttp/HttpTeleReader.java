package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleReader;

/**
 * Basic reader for retrieving param vales from http request
 */
@FunctionalInterface
public interface HttpTeleReader<V, R extends HttpReadOptions> extends TeleReader<V, R> {

}
