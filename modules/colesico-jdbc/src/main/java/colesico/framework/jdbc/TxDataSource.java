/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DataSource proxy is indent for transaction management support.
 * This DataSource can be used for transaction support within third-party libraries integration.
 */
public class TxDataSource extends DataSourceProxy {

    protected final JdbcTransactionalShell txShell;

    public TxDataSource(JdbcTransactionalShell txShell) {
        this.txShell = txShell;
    }

    @Override
    public DataSource getPrimaryDataSource() {
        return txShell.getDataSource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new TxConnection(txShell);
    }

    public static class TxConnection extends ConnectionProxy {

        protected final JdbcTransactionalShell txShell;

        public TxConnection(JdbcTransactionalShell txShell) {
            this.txShell = txShell;
        }

        @Override
        public Connection getPrimaryConnection() {
            return txShell.getConnection();
        }

        @Override
        public void close() throws SQLException {
            throw new UnsupportedOperationException("Directly connection close is not supported. Use transaction control instead.");
        }

        @Override
        public void commit() throws SQLException {
            throw new UnsupportedOperationException("Directly connection commit is not supported. Use transaction control instead.");
        }

        @Override
        public void rollback() throws SQLException {
            throw new UnsupportedOperationException("Directly connection rolling back is not supported. Use transaction control instead.");
        }

    }

}
