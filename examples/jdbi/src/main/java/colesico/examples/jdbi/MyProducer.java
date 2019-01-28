package colesico.examples.jdbi;

import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.ioc.Unscoped;
import colesico.framework.jdbi.JdbiTransactionalShell;
import colesico.framework.transaction.TransactionalShell;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Singleton;

@Producer
public class MyProducer {


    /**
     * Define transactional shell to control transactions.
     *
     * @return
     */
    @Singleton
    public TransactionalShell getTransactionalShell(@Classed(MyJdbiSettings.class) Jdbi jdbi) {
        return new JdbiTransactionalShell(jdbi);
    }

    /**
     * Define handle producing based on transactional shell
     */
    @Unscoped
    public Handle getHandle(TransactionalShell txShell) {
        return ((JdbiTransactionalShell) txShell).getHandle();
    }

}
