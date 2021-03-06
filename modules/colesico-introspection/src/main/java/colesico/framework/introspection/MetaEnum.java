package colesico.framework.introspection;

/**
 * Enum introspection
 *
 * @param <E> target enum
 */
public interface MetaEnum<E> extends MetaInterface<E> {

    MetaEnumConstant<E>[] getConstants();

    @Override
    default Kind getKind() {
        return Kind.ENUM;
    }
}
