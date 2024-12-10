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
    protected Integer corePoolSize = 5;

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
