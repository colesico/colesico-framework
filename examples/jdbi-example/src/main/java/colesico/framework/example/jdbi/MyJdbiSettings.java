package colesico.framework.example.jdbi;

import colesico.framework.config.Configuration;
import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polysupplier;
import colesico.framework.jdbi.JdbiOptions;
import colesico.framework.jdbi.DefaultJdbiConfig;

import javax.sql.DataSource;


@Configuration
public class MyJdbiSettings extends DefaultJdbiConfig {

    // jDBI will use  hikari data source configured with hikari.properties file
    public MyJdbiSettings(@Classed(HikariProperties.class) DataSource dataSource, Polysupplier<JdbiOptions> options) {
        super(dataSource, options);
    }
}
