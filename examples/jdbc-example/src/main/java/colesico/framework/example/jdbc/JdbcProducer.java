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

package colesico.framework.example.jdbc;

import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Unscoped;
import colesico.framework.jdbc.JdbcTransactionalShell;
import colesico.framework.transaction.TransactionalShell;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Typical jdbc producer
 */
@Producer
public class JdbcProducer {

    /**
     * Define transactional shell to control transactions.
     * DataSource is a HikariCP data source configured by hikari.properties file
     */
    @Singleton
    public TransactionalShell getTransactionalShell(@Classed(HikariProperties.class) DataSource ds) {
        return new JdbcTransactionalShell(ds);
    }

    /**
     * Define connection providing from transactional shell
     */
    @Unscoped
    public Connection getConnection(TransactionalShell txShell) {
        return ((JdbcTransactionalShell) txShell).getConnection();
    }

    /**
     * Optionally define the data source providing
     */
    @Unscoped
    public DataSource getDataSource(TransactionalShell txShell) {
        return ((JdbcTransactionalShell) txShell).getDataSource();
    }

}
