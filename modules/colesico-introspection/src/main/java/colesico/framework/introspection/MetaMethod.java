package colesico.framework.introspection;

/**
 * A method introspection
 *
 * @param <C> composite type that contains this method
 * @param <R> return type
 */
public interface MetaMethod<C, R> extends MetaElement {

    Class<R> getReturnType();

    MetaParameter[] getParameters();

    R invoke(C instance, Object... args);

    @Override
    default Kind getKind() {
        return Kind.METHOD;
    }

    @FunctionalInterface
    interface invoker<C> {
        C invoke(C instance, Object... args);
    }
}
