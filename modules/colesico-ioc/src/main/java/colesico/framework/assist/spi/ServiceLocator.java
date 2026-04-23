package colesico.framework.assist.spi;

import java.util.Iterator;
import java.util.Set;

/**
 * {@link java.util.ServiceLoader} alternative loader
 *
 * @param <S> service type
 */
public interface ServiceLocator<S> extends Iterable<S> {

    /**
     * Scan application classpath/modules for find service providers
     */
    Set<S> providers();

    @Override
    default Iterator<S> iterator() {
        return providers().iterator();
    }
}
