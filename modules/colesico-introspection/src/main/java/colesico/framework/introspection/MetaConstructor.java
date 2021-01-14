package colesico.framework.introspection;

/**
 * Class constructor introspection
 *
 * @param <C> class that contains this constructor
 */
public interface MetaConstructor<C> extends MetaElement {

    MetaParameter[] getParameters();

    C instantiate(Object... args);

    @Override
    default Kind getKind() {
        return Kind.CONSTRUCTOR;
    }

    @FunctionalInterface
    interface Instantiator<C> {
        C instantiate(Object... args);
    }

}