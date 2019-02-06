package colesico.framework.example.transaction;

import colesico.framework.transaction.TransactionalShell;
import colesico.framework.transaction.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransctionalShellMock implements TransactionalShell<Object> {

    Logger log = LoggerFactory.getLogger(TransctionalShellMock.class);

    final String executorName;

    public TransctionalShellMock(String executorName) {
        this.executorName = executorName;
    }

    @Override
    public <R> R required(UnitOfWork<R> unitOfWork, Object options) {
        log.info("Required begin |" + executorName);
        R result = unitOfWork.execute();
        log.info("Required end |" + executorName);
        return result;
    }

    @Override
    public <R> R requiresNew(UnitOfWork<R> unitOfWork, Object options) {
        log.info("RequiresNew begin |" + executorName);
        R result = unitOfWork.execute();
        log.info("RequiresNew end |" + executorName);
        return result;
    }

    @Override
    public <R> R mandatory(UnitOfWork<R> unitOfWork, Object options) {
        log.info("Mandatory begin");
        R result = unitOfWork.execute();
        log.info("Mandatory end");
        return result;
    }

    @Override
    public <R> R notSupported(UnitOfWork<R> unitOfWork, Object options) {
        log.info("NotSupported begin");
        R result = unitOfWork.execute();
        log.info("NotSupported end");
        return result;
    }

    @Override
    public <R> R supports(UnitOfWork<R> unitOfWork, Object options) {
        log.info("Supports begin");
        R result = unitOfWork.execute();
        log.info("Supports end");
        return result;
    }

    @Override
    public <R> R never(UnitOfWork<R> unitOfWork, Object options) {
        log.info("Never begin");
        R result = unitOfWork.execute();
        log.info("Never end");
        return result;
    }

    @Override
    public <R> R nested(UnitOfWork<R> unitOfWork, Object options) {
        log.info("Nested begin");
        R result = unitOfWork.execute();
        log.info("Nested end");
        return result;
    }
}
