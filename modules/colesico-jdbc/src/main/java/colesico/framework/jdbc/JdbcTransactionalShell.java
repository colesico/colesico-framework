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

import colesico.framework.jdbc.internal.JdbcTransaction;
import colesico.framework.transaction.AbstractTransactionalShell;
import colesico.framework.transaction.Tuning;
import colesico.framework.transaction.UnitOfWork;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Transactional shell simple implementation.
 * This shell controls local transactions with jdbc connection.
 * Nested transactions are not supported.
 */
public class JdbcTransactionalShell extends AbstractTransactionalShell<JdbcTransaction, Tuning<Connection>> {

    protected final DataSource dataSource;

    /**
     * Data source will be used to obtain connections to database to bind them with transaction.
     *
     * @param dataSource
     */
    public JdbcTransactionalShell(DataSource dataSource) {
        super(LoggerFactory.getLogger(JdbcTransactionalShell.class));
        this.dataSource = dataSource;
    }

    /**
     * Creates new transaction and execute unit of work within that transaction/
     * If current trunsaction is active throws Illegal state exception
     *
     * @param unitOfWork
     * @param tuning
     * @param <R>
     * @return
     */
    @Override
    protected <R> R createNew(UnitOfWork<R> unitOfWork, Tuning<Connection> tuning) {
        if (transactions.get() != null) {
            throw new IllegalStateException("Active JDBC transaction exists");
        }
        JdbcTransaction tx = new JdbcTransaction().setTuning(tuning);
        transactions.set(tx);

        Connection connection = null;
        try {
            R result = unitOfWork.execute();
            connection = tx.getConnection();
            if (connection != null) {
                if (tx.getRollbackOnly()) {
                    connection.rollback();
                } else {
                    connection.commit();
                }
            }
            return result;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception rbe) {
                    logger.error("Error rolling back JDBC connection: " + ExceptionUtils.getRootCauseMessage(rbe));
                }
            }
            throw rethrow(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.error("Error closing JDBC connection: " + ExceptionUtils.getRootCauseMessage(e));
                }
            }
            transactions.remove();
        }
    }

    @Override
    public void setRollbackOnly() {
        JdbcTransaction tx = getTransaction();
        tx.setRollbackOnly(true);
    }

    /**
     * Returns shell bound primary data source that are used to obtain connections to database.
     * This method is indent to use in custom data sources that supports the framework transactions control.
     *
     * @return
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Returns transaction bound connection.
     * This connection is obtained from primary data source and bound to current transaction.
     *
     * @return
     */
    public Connection getConnection() {
        JdbcTransaction tx = getTransaction();
        Connection connection = tx.getConnection();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                if (connection.getAutoCommit()) {
                    connection.setAutoCommit(false);
                }

                if (tx.getTuning() != null) {
                    tx.getTuning().applyTuning(connection);
                }

            } catch (Exception e) {
                logger.error("Error creating JDBC connection: " + ExceptionUtils.getRootCauseMessage(e));
                rethrow(e);
            }
            tx.setConnection(connection);
        }

        return connection;
    }


}
