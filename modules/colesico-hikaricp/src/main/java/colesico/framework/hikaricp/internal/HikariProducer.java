package colesico.framework.hikaricp.internal;

import colesico.framework.hikaricp.HikariConfigPrototype;
import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.*;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.sql.DataSource;

import static colesico.framework.ioc.Rank.RANK_MINOR;

@Producer(RANK_MINOR)
public class HikariProducer {

    protected final Logger log = LoggerFactory.getLogger(HikariProducer.class);

    /**
     * HikariDataSource factory
     * Creates HikariCP data source configured with config
     *
     * @return
     */
    @Classed(HikariConfigPrototype.class)
    @Unscoped
    public DataSource hikariDataSourceFactory(@Message HikariConfigPrototype settings) {
        try {
            HikariDataSource dataSource = new HikariDataSource(settings.getDataSourceConfig());
            log.debug("Hikari DB connection pool has been created with configuration: " + settings);
            return dataSource;
        } catch (Exception e) {
            log.error("Error initializing Hikari DB connection pool: " + ExceptionUtils.getRootCauseMessage(e) +
                    "; Configuration: " + settings);
            throw new RuntimeException(e);
        }
    }

    /**
     * Produces HikariDataSource for default HikariCP configuration
     * defined in file META-INF/hikaricp.properties
     *
     * @param hdsFactory
     * @return
     */
    @Singleton
    @Classed(HikariProperties.class)
    public DataSource propertiesBasedHikariDataSource(@Classed(HikariConfigPrototype.class) Supplier<DataSource> hdsFactory) {
        return hdsFactory.get(new HikariProperties());
    }

}
