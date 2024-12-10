package colesico.framework.task;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;

/**
 * Default config
 * Override this with {@link colesico.framework.ioc.conditional.Substitute}
 */
@Config
@Substitute(Substitution.DUMMY)
public class DefaultTaskVTExecutorConfig extends TaskVTExecutorConfigPrototype {
}
