package colesico.framework.profile;

import java.util.Collection;
import java.util.Map;

/**
 * Low-level profile utilities for profile source.
 * It is a tooling facade for the profile  implementation.
 */
public interface ProfileUtils<P extends Profile> {

    /**
     * Create profile instance
     *
     * @param attributes  can be null
     * @param preferences can b null
     */
    P create(Collection<?> attributes, Collection<?> preferences);

    default P create(Collection<?> attributes) {
        return create(attributes, null);
    }

    Collection<?> getPreferences(P profile);

    Collection<?> getAttributes(P profile);

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

    Collection<?> fromBytes(byte[] properties);

}
