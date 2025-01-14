package colesico.framework.profile;

import colesico.framework.profile.internal.LocalePropertyUtils;

import java.util.Locale;
import java.util.Map;

/**
 * Low-level profile utilities for profile source.
 * It is an instrumental facade for the profile implementation.
 */
public interface ProfileUtils {

    /**
     * Create profile instance.
     */
    colesico.framework.profile.Profile createProfile(Map<String, ?> properties);

    /**
     * Map of profile all properties (attributes + preferences).
     * property_name => property_value
     *
     * @see PropertyUtils#getName()
     * @see PropertyUtils#getValue(colesico.framework.profile.Profile)
     */
    Map<String, ?> getProperties(Profile profile);

    Map<String, ?> getAttributes(Profile profile);

    Map<String, ?> getPreferences(Profile profile);

    /**
     * Converts profile properties to tags map: property-name=> tag
     */
    Map<String, String> toTags(Map<String, ?> properties);

    /**
     * Converts tags to properties object collection
     */
    Map<String, ?> fromTags(Map<String, String> tags);

    /**
     * Converts profile properties to serialized array
     */
    byte[] toBytes(Map<String, ?> properties);

    /**
     * Properties bytes to properties map
     */
    Map<String, ?> fromBytes(byte[] bytes);

    default byte[] serialize(Profile profile) {
        return toBytes(getProperties(profile));
    }

    default colesico.framework.profile.Profile deserialize(byte[] profileBytes) {
        return createProfile(fromBytes(profileBytes));
    }

    /**
     * Useful for testing purposes (mockup profile)
     */
    default colesico.framework.profile.Profile fromLocale(Locale locale) {
        return createProfile(Map.of(LocalePropertyUtils.PROPERTY_NAME, locale));
    }


}
