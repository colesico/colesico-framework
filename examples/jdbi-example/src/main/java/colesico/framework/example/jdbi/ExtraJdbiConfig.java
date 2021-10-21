package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.jdbi.AbstractJdbiConfig;
import colesico.framework.jdbi.JdbiOptionsPrototype;

import javax.inject.Inject;
import javax.sql.DataSource;

@Config
public class ExtraJdbiConfig extends AbstractJdbiConfig {

    @Inject
    public ExtraJdbiConfig(

            // Jdbi will use extra hikaricp data source
            @Classed(ExtraHikariProperties.class) DataSource dataSource,

            // Optional configurations will be applied to the jdbi instance.
            @Classed(ExtraJdbiConfig.class) Polysupplier<JdbiOptionsPrototype> options) {

        super(dataSource, options);
    }
}
