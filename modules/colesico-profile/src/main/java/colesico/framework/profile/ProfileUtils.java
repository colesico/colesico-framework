package colesico.framework.profile;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Low-level profile utilities for profile source.
 * It is an instrumental facade for the profile implementation.
 */
public interface ProfileUtils {

    /**
     * Create profile instance.
     *
     * @param values profile fields values
     */
    Profile createProfile(Map<String, Object> values);

    /**
     * Create profile with default properties
     */
    Profile createProfile();

    default Profile createProfile(Map<String, Object> attributes, Map<String, Object> preferences) {
        Map<String, Object> values = new HashMap<>(attributes);
        values.putAll(preferences);
        return createProfile(values);
    }

    /**
     * Useful for testing purposes (mockup profile)
     */
    default Profile createProfile(Locale locale) {
        return createProfile(Map.of(DefaultProfile.LOCALE_PROPERTY, locale));
    }

    /**
     * Get values of profile fields (attributes + preferences).
     * property_name => field_value
     *
     * @see ProfileValueUtils#getName()
     * @see ProfileValueUtils#getValue(colesico.framework.profile.Profile)
     */
    Map<String, Object> getValues(Profile profile);

    Map<String, Object> getAttributes(Profile profile);

    Map<String, Object> getPreferences(Profile profile);

    /**
     * Converts profile  fields object values to string properties map: property-name=> value
     */
    Map<String, String> toProperties(Map<String, Object> values);

    /**
     * Converts profile string properties to fields object values
     */
    Map<String, Object> fromProperties(Map<String, String> properties);

    /**
     * Converts profile fields values to serialized array
     */
    byte[] toBytes(Map<String, Object> values);

    /**
     * Profile properties bytes to fields values map
     */
    Map<String, Object> fromBytes(byte[] bytes);

    default byte[] serialize(Profile profile) {
        return toBytes(getValues(profile));
    }

    default Profile deserialize(byte[] bytes) {
        return createProfile(fromBytes(bytes));
    }
}
