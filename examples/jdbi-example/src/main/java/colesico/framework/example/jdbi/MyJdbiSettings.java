package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polysupplier;
import colesico.framework.jdbi.JdbiOptionsPrototype;
import colesico.framework.jdbi.DefaultJdbiConfig;

import javax.sql.DataSource;


@Config
public class MyJdbiSettings extends DefaultJdbiConfig {

    // jDBI will use  hikari data source configured with hikari.properties file
    public MyJdbiSettings(@Classed(HikariProperties.class) DataSource dataSource, Polysupplier<JdbiOptionsPrototype> options) {
        super(dataSource, options);
    }
}
