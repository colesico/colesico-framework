package colesico.framework.asynctask;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Task schedule configuration prototype
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class TaskScheduleConfigPrototype extends TaskExecutorConfigBase {
}
