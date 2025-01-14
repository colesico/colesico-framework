package colesico.framework.profile;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class ProfileConfigPrototype {

    abstract public Locale getDefaultLocale();

    /**
     * Profile instance factory
     */
    public Profile createNewProfile() {
        return new DefaultProfile();
    }

    /**
     * Create default properties
     */
    public Map<String, ?> createDefaultProperties() {
        Map<String, ? super Object> properties = new HashMap<>();
        properties.put(DefaultProfile.LOCALE_PROPERTY, getDefaultLocale());
        return properties;
    }
}
