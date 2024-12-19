package colesico.framework.profile;


import java.util.Locale;

/**
 * Profile preferences control
 */
public interface ProfilePreferences extends Iterable {

    boolean isEmpty();

    <T> boolean contains(Class<T> propertyClass);

    /**
     * Set profile property as preference.
     *
     * @see ProfileKit#commit(ProfilePreferences)
     */
    <T> T add(T property);

    /**
     * Exclude profile property from preferred (but not delete form profile properties)
     *
     * @see ProfileKit#commit(ProfilePreferences)
     */
    <T> T exclude(Class<T> propertyClass);

    void clear();

    /**
     * Return underlying profile
     */
    Profile profile();

    /**
     * Set preferred locale
     */
    default void setLocale(Locale locale) {
        add(locale);
    }
}
