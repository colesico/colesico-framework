package colesico.framework.task;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Task virtual thread execution configuration prototype
 */
@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class TaskVTExecutorConfigPrototype extends AbstractTaskExecutorConfig {

}
