package colesico.framework.profile;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class ProfileConfigPrototype {

    /**
     * Profile instance factory
     */
    abstract public Profile createNewProfile();

    /**
     * Create profile  fields default values
     */
    abstract public Map<String, Object> createDefaultValues();
}
