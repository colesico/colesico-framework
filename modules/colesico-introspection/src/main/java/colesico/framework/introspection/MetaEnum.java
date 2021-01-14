package colesico.framework.introspection;

/**
 * Enum introspection
 * @param <C> target enum
 */
public interface MetaEnum<C> extends MetaInterface<C> {

    @Override
    default Kind getKind() {
        return Kind.ENUM;
    }
}
