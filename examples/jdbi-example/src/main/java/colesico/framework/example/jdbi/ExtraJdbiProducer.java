package colesico.framework.example.jdbi;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.jdbi.JdbiTransactionalShell;
import colesico.framework.transaction.TransactionalShell;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * To produce extra jdbi handle instance and tx shell
 */
@Producer
public class ExtraJdbiProducer {

    public static final String EXTRA="extra";

    /**
     * Produce extra transactional shell to control transactions.
     */
    @Singleton
    @Named(EXTRA)
    public TransactionalShell getTxShell(@Classed(ExtraJdbiConfig.class) Jdbi jdbi) {
        return new JdbiTransactionalShell(jdbi);
    }

    /**
     * Produce extra handle providing from extra transactional shell
     */
    @Unscoped
    @Named(EXTRA)
    public Handle getHandle(@Named(EXTRA) TransactionalShell txShell) {
        return ((JdbiTransactionalShell) txShell).getHandle();
    }
}
