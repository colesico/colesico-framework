package colesico.framework.introspection;

/**
 * Class introspection
 *
 * @param <C> target class
 */
public interface MetaClass<C> extends MetaInterface<C> {

    MetaConstructor<C>[] getConstructors();

    @Override
    default Kind getKind() {
        return Kind.CLASS;
    }
}
