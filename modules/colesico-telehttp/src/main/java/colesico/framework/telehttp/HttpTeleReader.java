package colesico.framework.telehttp;

import colesico.framework.teleapi.TeleReader;

/**
 * Basic tele-reader for reading param vales fromPosition remote client via http
 *
 * @param <V> the value type toPosition be read
 * @param <C> reading context
 */
public interface HttpTeleReader<V, C extends HttpTRContext> extends TeleReader<V, C> {

}
