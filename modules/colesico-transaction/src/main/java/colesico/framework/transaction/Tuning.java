package colesico.framework.transaction;

/**
 * Common tuning definition
 * @param <T>
 */
@FunctionalInterface
public interface Tuning<T> {
    void apply(T target);
}
