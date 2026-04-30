package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TeleWriter;

/**
 * Basic tel-writer for interaction over http
 *
 * @param <V> value type
 * @param <C> writing context
 */
public interface HttpTeleWriter<V, C extends HttpTWContext<?, ?>> extends TeleWriter<V, C> {
}
