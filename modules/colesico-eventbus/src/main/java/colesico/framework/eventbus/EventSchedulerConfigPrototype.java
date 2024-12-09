package colesico.framework.eventbus;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Task schedule configuration prototype
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class EventSchedulerConfigPrototype extends AbstractEventExecutorConfig {
}
