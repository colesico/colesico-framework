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

package colesico.framework.jdbi.internal;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbi.DefaultJdbiConfig;
import colesico.framework.jdbi.JdbiTransactionalShell;
import colesico.framework.transaction.TransactionalShell;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Singleton;
import java.sql.Connection;

import static colesico.framework.ioc.conditional.Substitution.STUB;

@Producer
@Substitute(STUB)
public class JdbiStubProducer {

    /**
     * Default transactional shell producing
     */
    @Singleton
    public TransactionalShell getDefaultTransactionalShell(@Classed(DefaultJdbiConfig.class) Jdbi jdbi) {
        return new JdbiTransactionalShell(jdbi);
    }

    /**
     * Default connection producing
     * Produce connection from jdbc tx shell as default connection
     */
    @Unscoped
    public Connection getDefaultConnection(TransactionalShell txShell) {
        return ((JdbiTransactionalShell) txShell).getHandle().getConnection();
    }

}