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

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbc.JdbcTransactionalShell;
import colesico.framework.transaction.TransactionalShell;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Typical jdbc producer for custom DS
 */
@Producer
public class JdbcProducer {

    /**
     * Define transactional shell to control transactions.
     * DataSource is a HikariCP data source configured by custom-custom-hikari.properties file
     */
    @Singleton
    @Substitute
    public TransactionalShell getTransactionalShell(@Classed(CustomHikariProperties.class) DataSource ds) {
        return new JdbcTransactionalShell(ds);
    }

}
