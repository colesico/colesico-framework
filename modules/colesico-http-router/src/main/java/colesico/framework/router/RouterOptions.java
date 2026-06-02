package colesico.framework.router;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Used to directly define custom routes actions
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class RouterOptions {
    abstract public void applyOptions(RouterBuilder builder);
}
