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
     * Attribute accessor metadata
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
     * Profile attribute metadata
     */
    class Metadata {
        public boolean dataPortReadable;
        public boolean dataPortWritable;

        // Reserved for profile storage support
        public boolean storageReadable;
        public boolean storageWritable;
    }
}
