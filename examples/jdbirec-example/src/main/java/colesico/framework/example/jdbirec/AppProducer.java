package colesico.framework.example.jdbirec;

import colesico.framework.ioc.production.Producer;
import colesico.framework.jdbi.JdbiTransactionManager;
import colesico.framework.transaction.TransactionManager;
import colesico.framework.transaction.Transactional;

@Producer
public class AppProducer {

    /**
     *  Default tx manager producing
     *  to omit specify manager name on {@link Transactional#manager()}
     */
    public TransactionManager defaultTransactionManager(JdbiTransactionManager jdbiTxManager) {
        return jdbiTxManager;
    }
}

