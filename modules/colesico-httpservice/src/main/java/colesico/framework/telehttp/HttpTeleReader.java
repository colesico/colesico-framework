package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleReader;

/**
 * Basic tele-reader for reading param vales from remote client via http
 *
 * @param <V> the value type to  be read
 * @param <C> reading context
 */
abstract public class HttpTeleReader<V, C extends HttpTeleContext<?, ?>> implements TeleReader<V, C> {

}
