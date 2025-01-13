package colesico.framework.assist.spi;

import java.util.function.Predicate;

public interface ServiceLocatorFactory {

    <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service, ClassLoader classLoader, Predicate<Class<? extends S>> classFilter);

    <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service, ClassLoader classLoader);

    <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service, Predicate<Class<? extends S>> classFilter);

    <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service);

    static ServiceLocatorFactory of() {
        return DefaultServiceLocatorFactory.of();
    }
}
