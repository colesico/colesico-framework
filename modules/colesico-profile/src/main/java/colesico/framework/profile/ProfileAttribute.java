package colesico.framework.profile;

/**
 * Profile attribute accessor
 */
public interface ProfileAttribute<V,M extends ProfileAttribute.Metadata> {

    /**
     * Attribute name.
     * Use snake_case notation.
     */
    String name();

    /**
     * Returns attribute metadata
     */
    M metadata();

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
     * Profile attribute meta information for
     * attribute processing configuration
     */
    interface Metadata {

        /**
         * Allow read attribute from source
         */
        boolean readable();

        /**
         * Allow to write attribute to source
         */
        boolean writable();
    }
}
