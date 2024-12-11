package colesico.framework.asynctask;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;

/**
 * Default config
 * Override this with {@link colesico.framework.ioc.conditional.Substitute}
 */
@Config
@Substitute(Substitution.STUB)
public class DefaultTaskExecutorConfig extends TaskExecutorConfigPrototype {
}
