package colesico.framework.teleapi;

import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;

/**
 *  Tele-method resolution for {@link TeleController#invoke(TeleResolution)}
 */
public interface TeleResolution<R extends TRContext, W extends TWContext> {
    TeleMethod<R,W> telemethod();
}
