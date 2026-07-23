package colesico.framework.example.jdbi;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbi.JdbiTransactionManager;
import colesico.framework.transaction.TransactionManager;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

/**
 * To produce extra jdbi handle instance and tx shell
 */
@Producer
public class ExtraJdbiProducer {

    public static final String EXTRA="jdbi-extra";

    /**
     * Produce extra transactional shell to control transactions.
     */
    @Singleton
    @Named(EXTRA)
    public TransactionManager extraTransactionalShell(@Classed(ExtraJdbiConfig.class) Jdbi jdbi) {
        return new JdbiTransactionManager(jdbi);
    }

    /**
     * Produce extra handle providing from extra transactional shell
     */
    @Unscoped
    @Named(EXTRA)
    public Handle extraHandle(@Named(EXTRA) TransactionManager txShell) {
        return ((JdbiTransactionManager) txShell).handle();
    }
}
