package colesico.framework.ioc.ioclet;

import colesico.framework.ioc.Supplier;

/**
 * Supplier default implementation
 * @param <T>
 */
public final class DefaultSupplier<T> implements Supplier<T> {

    private final Factory<T> factory;

    public DefaultSupplier(Factory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T get(Object message) {
        return factory.get(message);
    }
}
