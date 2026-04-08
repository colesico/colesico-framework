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
     * Returns attribute metadata
     */
    Metadata metadata();

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
     * Profile attribute meta information
     */
    interface Metadata {

        /**
         * Allow read attribute from source
         */
        boolean readable();

        /**
         * Allow write attribute to source
         */
        boolean writable();

    }
}
