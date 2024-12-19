package colesico.framework.profile;


/**
 * Profile preferences API
 */
public interface Preferences extends Iterable {

    boolean isEmpty();

    <T> boolean contains(Class<T> propertyClass);

    void clear();

    /**
     * Set profile property as preference.
     *
     * @see ProfileKit#commit(Profile)
     */
    <T> T set(T property);

    /**
     * Remove profile property from preferred.
     *
     * @see ProfileKit#commit(Profile)
     */
    <T> T remove(Class<T> propertyClass);
}
