package colesico.framework.transaction;

/**
 * Allows the user to execute a code in the context of a transaction.
 * Transaction configuration (isolation level, timeout , etc) should be done at the shell instance level.
 * Separate transaction configuration per execution is configured with the tuning.
 *
 * @param <U> any transaction tuning
 */
public interface TransactionalShell<U> {

    String REQUIRED_METHOD = "required";
    String REQUIRES_NEW_METHOD = "requiresNew";
    String MANDATORY_METHOD = "mandatory";
    String NOT_SUPPORTED_METHOD = "notSupported";
    String SUPPORTS_METHOD = "supports";
    String NEVER_METHOD = "never";
    String NESTED_METHOD = "nested";


    /**
     * Support a current transaction, create a new one if none exists.
     *
     * @param unitOfWork
     * @param <R>
     * @return
     */
    <R> R required(UnitOfWork<R> unitOfWork, U tuning);

    default <R> R required(UnitOfWork<R> unitOfWork) {
        return required(unitOfWork, null);
    }

    /**
     * Create a new transaction, and suspend the current transaction if one exists.
     *
     * @param unitOfWork
     * @param <R>
     * @return
     */
    <R> R requiresNew(UnitOfWork<R> unitOfWork, U tuning);

    default <R> R requiresNew(UnitOfWork<R> unitOfWork) {
        return requiresNew(unitOfWork, null);
    }


    /**
     * Support a current transaction, throw an exception if none exists.
     *
     * @param unitOfWork
     * @param <R>
     * @return
     */
    <R> R mandatory(UnitOfWork<R> unitOfWork, U tuning);

    default <R> R mandatory(UnitOfWork<R> unitOfWork) {
        return mandatory(unitOfWork, null);
    }


    /**
     * Execute non-transactionally, suspend the current transaction if one exists.
     *
     * @param unitOfWork
     * @param <R>
     * @return
     */
    <R> R notSupported(UnitOfWork<R> unitOfWork, U tuning);

    default <R> R notSupported(UnitOfWork<R> unitOfWork) {
        return notSupported(unitOfWork, null);
    }

    /**
     * Support a current transaction, execute non-transactionally if none exists.
     *
     * @param unitOfWork
     * @param <R>
     * @return
     */
    <R> R supports(UnitOfWork<R> unitOfWork, U tuning);

    default <R> R supports(UnitOfWork<R> unitOfWork) {
        return supports(unitOfWork, null);
    }

    /**
     * Execute non-transactionally, throw an exception if a transaction exists.
     *
     * @param unitOfWork
     * @param <R>
     * @return
     */
    <R> R never(UnitOfWork<R> unitOfWork, U tuning);

    default <R> R never(UnitOfWork<R> unitOfWork) {
        return never(unitOfWork, null);
    }

    /**
     * Execute within a nested transaction if a current transaction exists, behave like required() else.
     *
     * @param unitOfWork
     * @param <R>
     * @return
     */
    <R> R nested(UnitOfWork<R> unitOfWork, U tuning);

    default <R> R nested(UnitOfWork<R> unitOfWork) {
        return nested(unitOfWork, null);
    }

    void setRollbackOnly();
}
