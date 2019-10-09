package colesico.framework.profile;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class ProfileConfigPrototype {
    /**
     * Return all possible qualifier names in the order of the profile qualifiers
     *
     * @return
     */
    abstract public String[] getQualifiersNames();

}
