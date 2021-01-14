package colesico.framework.introspection;

/**
 * Method od constructor parameter introspection
 *
 * @param <T> parameter type
 */
public interface MetaParameter<T> extends MetaElement {

    /**
     * Parameter type class object
     */
    Class<T> getType();

    @Override
    default Kind getKind() {
        return Kind.PARAMETER;
    }
}
