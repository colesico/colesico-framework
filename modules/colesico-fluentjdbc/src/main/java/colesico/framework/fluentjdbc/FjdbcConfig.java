package colesico.framework.fluentjdbc;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;

@ConfigPrototype(model = ConfigModel.MESSAGE, target = Fjdbc.class)
abstract public class FjdbcConfig {
    abstract public void applyOptions(FluentJdbcBuilder builder);
}
