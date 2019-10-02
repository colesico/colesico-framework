package colesico.framework.ioc.ioclet;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.Key;
import colesico.framework.ioc.UnsatisfiedInjectionException;
import org.slf4j.Logger;

/**
 * IoC container "instrumental" interface.
 * Should be used in Ioclet only
 */
public interface AdvancedIoc extends Ioc {

    String FACTORY_METHOD = "factory";
    String FACTORY_OR_NULL_METHOD = "factoryOrNull";

    /**
     * Returns factory by key.
     *
     * @return the factory if the appropriate is found, exception otherwise
     * @throws UnsatisfiedInjectionException
     */
    <T> Factory<T> factory(Key<T> key) throws UnsatisfiedInjectionException;

    /**
     * Returns factory by key.
     *
     * @return the factory if the appropriate is found, null otherwise
     */
    <T> Factory<T> factoryOrNull(Key<T> key);

}
