package colesico.framework.profile;

import java.util.Map;

/**
 * Profile attribute accessor.
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
    V getValue();

    default boolean hasValue() {
        return getValue() != null;
    }

    /**
     * Set profile attribute value
     */
    void setValue(V value);

    /**
     * Returns profile attribute value as string
     */
    String getString();

    /**
     * Set  profile attribute value from string
     */
    void setString(String value);

    /**
     * Returns profile attribute value as bytes
     */
    byte[] getBytes();

    /**
     * Set  profile attribute value from bytes
     */
    void setBytes(byte[] bytes);

    /**
     * Attribute meta information
     * to configure attribute processing
     */
    Map<String, Object> metadata();
}
