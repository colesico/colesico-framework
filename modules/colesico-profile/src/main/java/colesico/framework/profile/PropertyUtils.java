package colesico.framework.profile;

/**
 * Profile property assistant
 */
public interface PropertyUtils<P extends Profile, V> {

    /**
     * Property name must be lowercase, match [a-z0-9], and start with a letter.
     */
    String getName();

    PropertyKind getKind();

    /**
     * Return property value
     */
    V getValue(P profile);

    /**
     * Set property value
     */
    void setValue(P profile, V value);

    /**
     * Convert property value to tag
     */
    String toTag(V value);

    /**
     * Convert property tag to value
     */
    V fromTag(String tag);

    /**
     * Convert property value to bytes
     */
    byte[] toBytes(V value);

    /**
     * Convert property bytes to value
     */
    V fromBytes(byte[] bytes);

    /**
     * Attributes are set on the calling side by the client application and
     * cannot be changed.
     * Preferences overrides the attributes and
     * stored on the client side.
     */
    enum PropertyKind {
        /**
         * Preference are stored on client side
         */
        PREFERENCE,
        /**
         * Attribute are defined on client side and cannot be overridden on server side
         */
        ATTRIBUTE
    }
}
