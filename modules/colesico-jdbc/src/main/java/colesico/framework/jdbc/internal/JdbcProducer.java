package colesico.framework.jdbc.internal;

import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbc.JdbcConditions;
import colesico.framework.jdbc.JdbcTransactionalShell;
import colesico.framework.transaction.TransactionalShell;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;

@Producer
public class JdbcProducer {

    /**
     * Default transactional shell
     */
    @Requires(JdbcConditions.DefaultTransactionalShell.class)
    @Singleton
    public TransactionalShell getDefaultTransactionalShell(DataSource ds) {
        return new JdbcTransactionalShell(ds);
    }

    /**
     * Default jdbc connection
     */
    @Requires(JdbcConditions.DefaultConnection.class)
    @Unscoped
    public Connection getDefaultConnection(TransactionalShell txShell) {
        return ((JdbcTransactionalShell) txShell).getConnection();
    }
}
