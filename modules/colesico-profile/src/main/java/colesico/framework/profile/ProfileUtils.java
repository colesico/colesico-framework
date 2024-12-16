package colesico.framework.profile;

import java.util.Collection;
import java.util.Map;

public interface ProfileUtils<P extends Profile> {

    /**
     * Create profile instance
     */
    P create(Collection<?> attributes, Collection<?> preferences);

    Collection<?> getPreferences(P profile);

    Collection<?> getAttributes(P profile);

    /**
     * Converts profile values to properties map: property_name => property_value
     * The property name must be lowercase, match [a-z0-9], and start with a letter.
     * <p>
     * Property values can contain any characters
     * </p>
     */
    Map<String, String> toProperties(Collection<?> values);

    /**
     * Converts properties to values
     */
    Collection<?> fromProperties(Map<String, String> properties);

    byte[] serialize(Collection<?> values);

    Collection<?> deserialize(byte[] values);

}
