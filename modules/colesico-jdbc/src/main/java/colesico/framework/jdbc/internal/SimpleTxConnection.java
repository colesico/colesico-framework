package colesico.framework.jdbc.internal;

import colesico.framework.jdbc.ConnectionProxy;
import colesico.framework.jdbc.SimpleJdbcTransactionalShell;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleTxConnection extends ConnectionProxy {

    protected final SimpleJdbcTransactionalShell simpleShell;

    public SimpleTxConnection(SimpleJdbcTransactionalShell simpleShell) {
        this.simpleShell = simpleShell;
    }

    @Override
    public Connection getPrimaryConnection() {
        JdbcTransaction tx = simpleShell.getTransaction();
        return tx.getConnection();
    }

    @Override
    public void close() throws SQLException {
        throw new UnsupportedOperationException("Close the transactional connection is not supported");
    }

    @Override
    public void commit() throws SQLException {
        throw new UnsupportedOperationException("Commit the transactional connection is not supported");
    }

    @Override
    public void rollback() throws SQLException {
        throw new UnsupportedOperationException("Rollback the transactional connection is not supported");
    }
}
