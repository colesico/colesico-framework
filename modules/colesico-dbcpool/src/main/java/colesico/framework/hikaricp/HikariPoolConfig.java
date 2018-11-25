package colesico.framework.hikaricp;


import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.dbcpool.DBCPool;

import java.util.Properties;

/**
 * File based abstract configuration
 */

@ConfigPrototype(model = ConfigModel.MESSAGE, target = DBCPool.class)
abstract public class HikariPoolConfig {
    abstract public Properties getProperties();
}
