/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.hikaricp.internal;

import colesico.framework.hikaricp.HikariConfigPrototype;
import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.*;
import colesico.framework.ioc.annotation.Classed;
import colesico.framework.ioc.annotation.Message;
import colesico.framework.ioc.annotation.Producer;
import colesico.framework.ioc.annotation.Unscoped;
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
    public DataSource hikariDataSourceFactory(@Message HikariConfigPrototype config) {
        try {
            HikariDataSource dataSource = new HikariDataSource(config.getHikariConfig());
            log.debug("Hikari DB connection pool has been created with configuration: " + config);
            return dataSource;
        } catch (Exception e) {
            log.error("Error initializing Hikari database connection pool: " + ExceptionUtils.getRootCauseMessage(e) + "; Configuration: " + config);
            throw new RuntimeException(e);
        }
    }

    /**
     * Produces HikariDataSource for default HikariCP configuration
     * defined in file ./directory/hikari.properties or resource META-INF/hikari.properties
     *
     * @param factory
     * @return
     */
    @Singleton
    @Classed(HikariProperties.class)
    public DataSource defaultHikariDataSource(@Classed(HikariConfigPrototype.class) Supplier<DataSource> factory) {
        return factory.get(new HikariProperties(){});
    }

}
