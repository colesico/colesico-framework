package colesico.framework.jdbc.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbc.JdbcTransactionManager;
import colesico.framework.transaction.TransactionManager;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.sql.Connection;

@Producer
@Produce(JdbcTransactionManager.class)
public class JdbcProducer {

    /**
     * Default transactionManager
     */
    @Singleton
    @Named(JdbcTransactionManager.NAME)
    public TransactionManager jdbcTransactionManager(JdbcTransactionManager jdbcTxManager) {
        return jdbcTxManager;
    }

    /**
     * Default jdbc connection
     */
    @Unscoped
    @Named(JdbcTransactionManager.NAME)
    public Connection jdbcConnection(JdbcTransactionManager jdbcTxManager) {
        return jdbcTxManager.connection();
    }
}
