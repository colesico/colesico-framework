package colesico.framework.profile;

import java.util.Collection;
import java.util.Map;

public interface ProfileUtils<P extends Profile> {

    /**
     * Create profile instance
     */
    P create(Collection<?> attributes, Collection<?> preferences);

    Collection<?> getPreference(P profile);

    Collection<?> getAttributes(P profile);

    Collection<?> fromTags(Map<String, String> tags);

    Map<String, String> toTags(Collection<?> items);

}
