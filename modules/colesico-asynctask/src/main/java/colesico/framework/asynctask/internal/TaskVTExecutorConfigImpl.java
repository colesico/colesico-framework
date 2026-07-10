package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.TaskVTExecutorConfigPrototype;
import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;

/**
 * Default config
 * Override this with {@link colesico.framework.ioc.conditional.Substitute}
 */
@Config
@Substitute(Substitution.STUB)
public class TaskVTExecutorConfigImpl extends TaskVTExecutorConfigPrototype {
}
