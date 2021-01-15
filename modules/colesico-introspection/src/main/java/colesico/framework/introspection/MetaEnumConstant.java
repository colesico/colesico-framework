package colesico.framework.introspection;

/**
 * Enum constant introspection
 *
 * @param <E> enum type
 */
public interface MetaEnumConstant<E> extends MetaElement {

    E getValue();

    @Override
    default Kind getKind() {
        return Kind.ENUM_CONSTANT;
    }

    @FunctionalInterface
    interface Getter<E> {
        E get();
    }
}
