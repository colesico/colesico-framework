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
     * Return all possible tag keys
     */
    Collection<String> getRegisteredTagKeys();

    Collection<?> fromTags(Map<String, String> tags);

    Map<String, String> toTags(Collection<?> items);

}
