package colesico.framework.introspection;

/**
 * A field introspection
 *
 * @param <C> composite type that contains this field
 * @param <T> field type
 */
public interface MetaField<C, T> extends MetaElement {

    Class<T> getType();

    T getValue(C instance);

    void setValue(C instance, T value);

    @Override
    default Kind getKind() {
        return Kind.FIELD;
    }

    @FunctionalInterface
    interface Getter<C, T> {
        T get(C instance);
    }

    @FunctionalInterface
    interface Setter<C, T> {
        void set(C instance, T value);
    }
}
