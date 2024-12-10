package colesico.framework.task;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Task schedule configuration prototype
 */
@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class TaskSchedulerConfigPrototype extends AbstractTaskExecutorConfig {

    /**
     * Initial task workers pool size
     */
    public int getCorePoolSize() {
        return 5;
    }

}
