package colesico.framework.jdbi;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import org.jdbi.v3.core.Jdbi;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class JdbiOptions {
    abstract public void apply(final Jdbi jdbi);
}