package colesico.framework.hikaricp;


import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import com.zaxxer.hikari.HikariConfig;

import javax.sql.DataSource;

/**
 * File based abstract configuration
 */

@ConfigPrototype(model = ConfigModel.MESSAGE, target = DataSource.class)
abstract public class HikariConfiguration {
    abstract public HikariConfig getDataSourceConfig();
}
