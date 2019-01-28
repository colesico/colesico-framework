package colesico.framework.jdbc;

import colesico.framework.transaction.TransactionalShell;
import colesico.framework.transaction.Tuning;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * JDBC based transactional shell
 */
public interface JdbcTransactionalShell extends TransactionalShell<Tuning<Connection>> {

    /**
     * Returns transaction aware datasource bound to this transaction shell
     *
     * @return
     */
    DataSource getTxDataSource();
}
