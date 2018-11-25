package colesico.framework.ioc;

/**
 * Automatically generated IoC message that can be used for example for Logger factories (or other)
 * to  get the class name for which the logger instance  is intended.
 *
 * @see Message
 */
public final class InjectionPoint {
    private final Class<?> targetClass;

    public InjectionPoint(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public String toString() {
        return "InjectionPoint{" +
                "targetClass=" + targetClass +
                '}';
    }
}
