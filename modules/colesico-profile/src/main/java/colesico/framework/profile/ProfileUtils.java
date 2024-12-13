package colesico.framework.profile;

import java.util.Collection;
import java.util.Map;

public interface ProfileUtils<P extends Profile> {

    /**
     * Create profile instance
     */
    P create(Collection<?> attributes, Collection<?> preferences);

    P -- create(Map<String, String> attributeTags, Map<String, String> preferenceTags);

    /**
     * Export preferences to string map : prefKey->prefValue
     */
    Map<String, String> getPreferenceTags(P profile);

    /**
     * Export attributes to string map : attrKey->attrValue
     */
    Map<String, String> getAttributeTags(P profile);


}
