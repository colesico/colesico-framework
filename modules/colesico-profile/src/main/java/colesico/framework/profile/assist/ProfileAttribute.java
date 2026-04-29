package colesico.framework.profile.assist;

import colesico.framework.profile.Profile;

/**
 * Profile attribute helper.
 * Used to serialize and deserialize an attribute value,
 * obtain metadata of attribute
 */
public interface ProfileAttribute<P extends Profile, V> {

    /**
     * Returns parent profile
     */
    P profile();

    /**
     * Attribute name.
     * Use snake_case notation.
     */
    String name();

    /**
     * Return profile attribute value
     */
    V value();

    default boolean hasValue() {
        return value() != null;
    }

    /**
     * Set profile attribute value
     */
    void setValue(V value);

    /**
     * Returns profile attribute value as string
     */
    String asString();

    /**
     * Set  profile attribute value from string
     */
    void setString(String value);

    /**
     * Returns profile attribute value as bytes
     */
    byte[] asBytes();

    /**
     * Set  profile attribute value from bytes
     */
    void setBytes(byte[] bytes);

}

