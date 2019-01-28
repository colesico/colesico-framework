package colesico.framework.transaction;

/**
 * Represents transactional code block
 * @param <R> execution result that to be returned to caller
 */
@FunctionalInterface
public interface UnitOfWork<R> {
    String EXECUTE_METHOD="execute";
    R execute();
}
