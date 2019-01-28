package colesico.framework.jdbc.internal;


import colesico.framework.jdbc.DataSourceProxy;
import colesico.framework.jdbc.JdbcTransactionalShell;
import colesico.framework.jdbc.SimpleJdbcTransactionalShell;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DataSource proxy is indent for transaction management support
 */
public class SimpleTxDataSource extends DataSourceProxy {

    protected final SimpleJdbcTransactionalShell txShell;

    public SimpleTxDataSource(SimpleJdbcTransactionalShell txShell) {
        this.txShell = txShell;
    }

    @Override
    public DataSource getPrimaryDataSource() {
        return txShell.getPrimaryDataSource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return txShell.getTxConnection();
    }
}
