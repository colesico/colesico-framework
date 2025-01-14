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
     * Return profile field value
     */
    V getValue(P profile);

    /**
     * Set profile field value
     */
    void setValue(P profile, V value);

    /**
     * Convert profile  field value to string property value
     */
    String toProperty(V value);

    /**
     * Convert profile string property  value to profile field value
     */
    V fromProperty(String property);

    /**
     * Convert profile field value to bytes
     */
    byte[] toBytes(V value);

    /**
     * Convert  bytes to profile field value
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
