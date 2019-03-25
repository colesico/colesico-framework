package colesico.framework.profile;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.profile.teleapi.ProfileTeleAssist;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class ProfileConfig {
    /**
     * Return all possible qualifier names in the order of the profile qualifiers
     *
     * @return
     */
    abstract public String[] getQualifiersNames();

}
