package colesico.framework.introspection;

/**
 * Interface introspection
 *
 * @param <C> target type
 */
public interface MetaInterface<C> extends MetaElement {

    /**
     * Target type class object
     */
    Class<C> getType();

    MetaField<C, ?>[] getFields();

    MetaMethod<C, ?>[] getMethods();

    @Override
    default Kind getKind() {
        return Kind.INTERFACE;
    }
}