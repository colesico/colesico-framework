package colesico.framework.jdbc;

import colesico.framework.jdbc.internal.JdbcTransaction;
import colesico.framework.jdbc.internal.SimpleTxConnection;
import colesico.framework.jdbc.internal.SimpleTxDataSource;
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
public class SimpleJdbcTransactionalShell extends AbstractTransactionalShell<JdbcTransaction, Tuning<Connection>> implements JdbcTransactionalShell {

    protected final DataSource primaryDataSource;

    public SimpleJdbcTransactionalShell(DataSource primaryDataSource) {
        super(LoggerFactory.getLogger(SimpleJdbcTransactionalShell.class));
        this.primaryDataSource = primaryDataSource;
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
                connection.commit();
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
    public DataSource getTxDataSource() {
        return new SimpleTxDataSource(this);
    }

    public DataSource getPrimaryDataSource() {
        return primaryDataSource;
    }

    public SimpleTxConnection getTxConnection() {
        JdbcTransaction tx = getTransaction();

        if (tx.getConnection() == null) {
            Connection connection = null;
            try {
                connection = primaryDataSource.getConnection();
                if (connection.getAutoCommit()) {
                    connection.setAutoCommit(false);
                }

                if (tx.getTuning() != null) {
                    tx.getTuning().apply(connection);
                }

            } catch (Exception e) {
                logger.error("Error creating JDBC connection: " + ExceptionUtils.getRootCauseMessage(e));
                rethrow(e);
            }

            tx.setConnection(connection);
        }

        return new SimpleTxConnection(this);
    }

}
