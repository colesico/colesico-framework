package colesico.framework.profile.internal;

import colesico.framework.profile.ProfileConfig;

public class ProfileConfigImpl extends ProfileConfig {

    @Override
    public String[] getQualifiersNames() {
        return new String[]{"L", "C"};
    }
}
