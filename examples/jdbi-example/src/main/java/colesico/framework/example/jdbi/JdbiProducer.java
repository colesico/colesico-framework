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

package colesico.framework.example.jdbi;

import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Unscoped;
import colesico.framework.jdbi.JdbiTransactionalShell;
import colesico.framework.transaction.TransactionalShell;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Singleton;

/**
 * Typical jdbi producer
 */
@Producer
public class JdbiProducer {

    /**
     * Define transactional shell to control transactions.
     */
    @Singleton
    public TransactionalShell getTransactionalShell(@Classed(JdbiConfig.class) Jdbi jdbi) {
        return new JdbiTransactionalShell(jdbi);
    }

    /**
     * Define handle providing from transactional shell
     */
    @Unscoped
    public Handle getHandle(TransactionalShell txShell) {
        return ((JdbiTransactionalShell) txShell).getHandle();
    }

}
