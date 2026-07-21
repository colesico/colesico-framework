package colesico.framework.jdbc.internal;

import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbc.JdbcTransactionalShell;
import colesico.framework.transaction.TransactionalShell;

import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;

import static colesico.framework.ioc.conditional.Substitution.STUB;

@Producer
public class JdbcProducer {

    /**
     * Default transactional shell
     */
    @Singleton
    @Substitute(STUB)
    public TransactionalShell defaultTransactionalShell(DataSource ds) {
        return new JdbcTransactionalShell(ds);
    }

    /**
     * Default jdbc connection
     */
    @Unscoped
    @Substitute(STUB)
    public Connection defaultConnection(TransactionalShell txs) {
        return ((JdbcTransactionalShell) txs).connection();
    }
}
