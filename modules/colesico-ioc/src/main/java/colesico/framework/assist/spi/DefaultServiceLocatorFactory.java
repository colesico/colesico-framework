package colesico.framework.assist.spi;

import java.util.function.Predicate;

public class DefaultServiceLocatorFactory implements ServiceLocatorFactory {

    private static final DefaultServiceLocatorFactory INSTANCE = new DefaultServiceLocatorFactory();

    public static DefaultServiceLocatorFactory of() {
        return INSTANCE;
    }

    public <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service, ClassLoader classLoader, Predicate<Class<? extends S>> classFilter) {
        return new DefaultServiceLocator<>(caller, service, classLoader, classFilter);
    }

    public <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service, ClassLoader classLoader) {
        return new DefaultServiceLocator<>(caller, service, classLoader, null);
    }

    public <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service, Predicate<Class<? extends S>> classFilter) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new DefaultServiceLocator<>(caller, service, classLoader, classFilter);
    }

    public <S> ServiceLocator<S> locator(Class<?> caller, Class<S> service) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new DefaultServiceLocator<>(caller, service, classLoader, null);
    }

}
