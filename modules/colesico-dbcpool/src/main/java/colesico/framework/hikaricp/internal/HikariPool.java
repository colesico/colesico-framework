/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.hikaricp.internal;

import colesico.framework.dbcpool.DBCPool;
import colesico.framework.hikaricp.HikariPoolConfig;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Unscoped;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DBCPool default implementation
 *
 * @author Vladlen Larionov
 */
@Unscoped
public final class HikariPool implements DBCPool {

    protected final Logger log = LoggerFactory.getLogger(HikariPool.class);
    protected final HikariDataSource dataSource;
    protected final String configClass;

    public HikariPool(@Message HikariPoolConfig configuration) {
        String configClassName = configuration!=null? configuration.getClass().getName():"null";
        try {
            this.configClass = configuration.getClass().getName();
            HikariConfig config = new HikariConfig(configuration.getProperties());
            dataSource = new HikariDataSource(config);
            log.debug("Hikari DB connection pool has been created with configuration: " + configClassName);
        } catch (Exception e) {
            log.error("Error initializing Hikari DB connection pool: " + ExceptionUtils.getRootCauseMessage(e) +
                    "; Configuration class: " + configClassName);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "HikariPool{" +
                "configClass='" + configClass + '\'' +
                '}';
    }
}
