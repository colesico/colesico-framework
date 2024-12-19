package colesico.framework.profile;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
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
    P createProfile(Collection<?> attributes, Collection<?> preferences);

    default P fromAttributes(Collection<?> attributes) {
        return createProfile(attributes, null);
    }

    default P fromPreferences(Collection<?> preferences) {
        return createProfile(null, preferences);
    }

    /**
     * Useful for testing purposes (mockup profile)
     */
    default P fromLocale(Locale locale) {
        return createProfile(List.of(locale), null);
    }

    Collection<?> getProperties(P profile);

    Collection<?> getAttributes(P profile);

    Collection<?> getPreferences(P profile);

    /**
     * Set profile property as attribute
     *
     * @return previous assigned property if exists
     */
    <T> T addAttribute(P profile, T property);

    /**
     * Set profile property as preference
     *
     * @return previous assigned property if exists
     */
    <T> T addPreference(P profile, T property);

    /**
     * Create profile preferences control
     */
    ProfilePreferences createPreferences(P profile);

    /**
     * Converts profile properties to tag map: tag-key=> tag-value
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

    default byte[] serialize(P profile) {
        byte[] attributes = toBytes(getAttributes(profile));
        byte[] preferences = toBytes(getProperties(profile));

        ByteBuffer buff = ByteBuffer.allocate(attributes.length + preferences.length + Integer.BYTES * 2);
        buff.putInt(attributes.length).put(attributes);
        buff.putInt(preferences.length).put(preferences);

        return buff.array();
    }

    default P deserialize(byte[] profileBytes) {
        ByteBuffer buff = ByteBuffer.wrap(profileBytes);
        byte[] attributes = new byte[buff.getInt()];
        buff.get(attributes);

        byte[] preferences = new byte[buff.getInt()];
        buff.get(preferences);

        return createProfile(fromBytes(attributes), fromBytes(preferences));
    }

}
