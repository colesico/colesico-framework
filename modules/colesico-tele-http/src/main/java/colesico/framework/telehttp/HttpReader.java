package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleReader;

/**
 * Basic reader for retrieving param values from http request
 */
@FunctionalInterface
public interface HttpReader<V, O extends HttpReadOptions> extends TeleReader<V, O> {

}