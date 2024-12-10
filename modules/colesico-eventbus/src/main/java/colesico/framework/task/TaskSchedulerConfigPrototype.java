package colesico.framework.task;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Task schedule configuration prototype
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class TaskSchedulerConfigPrototype extends AbstractTaskExecutorConfig {
}
