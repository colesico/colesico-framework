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

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbc.JdbcTransactionalShell;
import colesico.framework.transaction.TransactionalShell;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Typical jdbc producer for extra DS
 */
@Producer
public class ExtraJdbcProducer {

    public static final String EXTRA = "extra";

    /**
     * Define extra transactional shell to control transactions.
     */
    @Singleton
    @Named(EXTRA)
    public TransactionalShell getTransactionalShell(@Classed(ExtraHikariProperties.class) DataSource ds) {
        return new JdbcTransactionalShell(ds);
    }

    /**
     * Produce extra connection providing from extra transactional shell
     */
    @Unscoped
    @Named(EXTRA)
    public Connection getConnection(@Named(EXTRA) TransactionalShell txShell) {
        return ((JdbcTransactionalShell) txShell).getConnection();
    }

    /**
     * Optionally produce extra data source from extra TxShell
     */
    @Unscoped
    @Named(EXTRA)
    public DataSource getDataSource(@Named(EXTRA) TransactionalShell txShell) {
        return ((JdbcTransactionalShell) txShell).getDataSource();
    }
}