/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.jdbi.internal;

import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbi.JdbiConfigPrototype;
import colesico.framework.jdbi.JdbiTransactionManager;
import colesico.framework.transaction.TransactionManager;
import jakarta.inject.Named;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import jakarta.inject.Singleton;

import java.sql.Connection;

@Producer
@Produce(JdbiTransactionManager.class)
public class JdbiProducer {

    /**
     * Jdbi factory
     * Creates Jdbi instance configured with settings
     */
    @Unscoped
    @Classed(JdbiConfigPrototype.class)
    public Jdbi jdbiFactory(@IocMessage JdbiConfigPrototype config) {
        final Jdbi jdbi = Jdbi.create(config.dataSource());
        config.configure(jdbi);
        return jdbi;
    }

    /**
     * Default Transaction Manager producing
     */
    @Singleton
    @Named(JdbiTransactionManager.NAME)
    public TransactionManager jdbiTransactionManager(JdbiTransactionManager jdbiTxManager) {
        return jdbiTxManager;
    }

    /**
     * Produce default handle providing from transaction manager
     */
    @Unscoped
    public Handle defaultHandle(JdbiTransactionManager jdbiTxManager) {
        return jdbiTxManager.handle();
    }

    /**
     * Default connection producing
     * Produce connection from jdbc tx manager as default connection
     */
    @Unscoped
    @Named(JdbiTransactionManager.NAME)
    public Connection jdbiConnection(JdbiTransactionManager jdbiTxManager) {
        return jdbiTxManager.handle().getConnection();
    }
}
