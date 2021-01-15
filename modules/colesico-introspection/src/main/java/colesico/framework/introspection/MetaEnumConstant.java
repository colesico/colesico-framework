package colesico.framework.introspection;

/**
 * Enum constant introspection
 *
 * @param <E> enum type
 */
public interface MetaEnumConstant<E> extends MetaElement {

    E getValue();

    @FunctionalInterface
    interface Getter<E> {
        E get();
    }
}
