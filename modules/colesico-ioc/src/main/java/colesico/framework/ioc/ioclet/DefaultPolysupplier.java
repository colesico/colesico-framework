package colesico.framework.ioc.ioclet;

import colesico.framework.ioc.IocException;
import colesico.framework.ioc.Polysupplier;

import java.util.Iterator;

/**
 * Polysupplier default implementation
 * @param <T>
 */
public final class DefaultPolysupplier<T> implements Polysupplier<T> {

    private final Factory<T> factory;


    public DefaultPolysupplier(Factory<T> factory) {
        this.factory = factory;
    }

    @Override
    public Iterator<T> iterator(Object message) {
        return new SupplierIterator(factory, message);
    }

    @Override
    public boolean isNotEmpty() {
        return factory != null;
    }

    static final class SupplierIterator<T> implements Iterator<T> {

        private Factory<T> factory;
        private final Object message;


        public SupplierIterator(Factory<T> factory, Object message) {
            this.factory = factory;
            this.message = message;
        }

        @Override
        public boolean hasNext() {
            return factory != null;
        }

        @Override
        public T next() {
            if (factory != null) {
                T instance = factory.get(message);
                factory = factory.next();
                return instance;
            } else {
                throw new IocException("Polysupplier hasn't more elements");
            }
        }
    }
}
