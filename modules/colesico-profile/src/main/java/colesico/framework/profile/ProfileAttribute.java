package colesico.framework.profile;

/**
 * Profile attribute accessor
 */
public interface ProfileAttribute<V> {

    /**
     * Attribute name.
     * Use snake_case notation.
     */
    String name();

    /**
     * Is readonly attribute.
     * Readonly attribute not intended for writing to {@link  ProfileSource}
     */
    boolean readonly();

    /**
     * Return profile attribute value
     */
    V getValue();

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
}
