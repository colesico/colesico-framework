package colesico.framework.profile;

import java.util.Collection;
import java.util.Map;

/**
 * Low-level profile utilities for profile source.
 * It is an instrumental facade for the profile implementation.
 */
public interface ProfileUtils<P extends Profile> {

    /**
     * Create profile instance.
     * Attributes are set on the calling side by the client application and
     * cannot be changed. Preferences are overrides of attributes that are also
     * stored on the client side.
     *
     * @param attributes  can be null
     * @param preferences can b null
     */
    P create(Collection<?> attributes, Collection<?> preferences);

    default P create(Collection<?> attributes) {
        return create(attributes, null);
    }
    
    Collection<?> getProperties(P profile);

    Collection<?> getAttributes(P profile);

    Collection<?> getPreferences(P profile);

    /**
     * Set profile property as attribute
     *
     * @return previous assigned property if exists
     */
    <T> T setAttribute(P profile, T property);

    /**
     * Set profile property as preference
     *
     * @return previous assigned property if exists
     */
    <T> T setPreference(P profile, T property);

    /**
     * Converts profile properties to properties map: tag-key=> tag-value
     * The tag-key must be lowercase, match [a-z0-9], and start with a letter.
     * <p>
     * Tag values can contain any characters
     * </p>
     */
    Map<String, String> toTags(Collection<?> properties);

    /**
     * Converts tags to properties object collection
     */
    Collection<?> fromTags(Map<String, String> tags);

    /**
     * Converts profile properties to serialized array
     */
    byte[] toBytes(Collection<?> properties);

    Collection<?> fromBytes(byte[] propertiesBytes);

}
