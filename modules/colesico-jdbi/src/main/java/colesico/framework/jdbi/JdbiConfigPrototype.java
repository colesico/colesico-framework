package colesico.framework.jdbi;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.ioc.Polysupplier;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;

@ConfigPrototype(model = ConfigModel.MESSAGE, target = Jdbi.class)
abstract public class JdbiConfigPrototype {

    abstract public DataSource getDataSource();

    abstract public Polysupplier<JdbiOptionsPrototype> getOptions();
}
