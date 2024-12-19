package colesico.framework.profile;


/**
 * Profile preferences API
 */
public interface Preferences extends Iterable {

    <T> boolean exists(Class<T> propertyClass);

    <T> T get(Class<T> propertyClass);

    /**
     * Set profile property as preference.
     *
     * @see ProfileKit#commit(Profile)
     */
    <T> T set(T property);

    /**
     * Set profile property as preference.
     *
     * @see ProfileKit#commit(Profile)
     */
    <T> T remove(Class<T> propertyClass);
}
