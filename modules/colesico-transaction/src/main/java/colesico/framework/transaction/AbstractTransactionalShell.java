package colesico.framework.transaction;

import org.slf4j.Logger;

/**
 * Abstract common transactional shell functionality
 *
 * @param <T> transaction type
 * @param <U> tuning type
 */
abstract public class AbstractTransactionalShell<T, U> implements TransactionalShell<U> {

    protected final Logger logger;

    protected final ThreadLocal<T> transactions = new ThreadLocal<>();

    public AbstractTransactionalShell(Logger logger) {
        this.logger = logger;
    }

    abstract protected <R> R createNew(UnitOfWork<R> unitOfWork, U tuning);

    @Override
    public <R> R required(UnitOfWork<R> unitOfWork, U tuning) {
        logger.debug("REQUIRED TX BEGIN");
        T tx = transactions.get();
        if (tx == null) {
            return createNew(unitOfWork, tuning);
        } else {
            return unitOfWork.execute();
        }
    }

    @Override
    public <R> R requiresNew(UnitOfWork<R> unitOfWork, U tuning) {
        logger.debug("REQUIRES_NEW TX BEGIN");
        T tx = transactions.get();
        if (tx == null) {
            return createNew(unitOfWork, tuning);
        } else {
            try {
                transactions.remove();
                return createNew(unitOfWork, tuning);
            } finally {
                transactions.set(tx);
            }
        }
    }

    @Override
    public <R> R mandatory(UnitOfWork<R> unitOfWork, U tuning) {
        logger.debug("MANDATORY TX BEGIN");
        getTransaction();
        return unitOfWork.execute();
    }

    @Override
    public <R> R notSupported(UnitOfWork<R> unitOfWork, U tuning) {
        logger.debug("NOT_SUPPORTED TX BEGIN");
        T tx = transactions.get();
        if (tx == null) {
            return unitOfWork.execute();
        } else {
            try {
                transactions.remove();
                return unitOfWork.execute();
            } finally {
                transactions.set(tx);
            }
        }
    }

    @Override
    public <R> R supports(UnitOfWork<R> unitOfWork, U tuning) {
        logger.debug("SUPPORTS TX BEGIN");
        return unitOfWork.execute();
    }

    @Override
    public <R> R never(UnitOfWork<R> unitOfWork, U tuning) {
        logger.debug("NEVER TX BEGIN");
        T tx = transactions.get();
        if (tx != null) {
            throw new IllegalStateException("Active transaction exists");
        }
        return unitOfWork.execute();
    }

    @Override
    public <R> R nested(UnitOfWork<R> unitOfWork, U tuning) {
        //For jdbc based transactions this can be implemented with savepoints (see jooq as example)
        throw new UnsupportedOperationException("Nested transactions is not supported.");
    }

    /**
     * Returns active transaction, trown exception otherwise
     *
     * @return
     */
    public T getTransaction() {
        T tx = transactions.get();
        if (tx == null) {
            throw new IllegalStateException("No active transaction");
        }
        return tx;
    }

    /**
     * Returns active transaction or null
     *
     * @return
     */
    public T getTransactionOrNull() {
        return transactions.get();
    }


    /**
     * Cast a CheckedException as an unchecked one.
     *
     * @param throwable to cast
     * @param <T>       the type of the Throwable
     * @return this method will never return a Throwable instance, it will just throw it.
     * @throws T the throwable as an unchecked throwable
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException rethrow(Throwable throwable) throws T {
        throw (T) throwable; // rely on vacuous cast
    }
}
