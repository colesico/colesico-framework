package colesico.framework.profile.internal;

import colesico.framework.profile.ProfileConfig;

import javax.inject.Singleton;

@Singleton
public class ProfileConfigImpl extends ProfileConfig {

    private static final String[] QUALIFIERS_NAMES = new String[]{"L", "C"};

    @Override
    public String[] getQualifiersNames() {
        return QUALIFIERS_NAMES;
    }

    @Override
    public String toString() {
        return "ProfileConfigImpl";
    }
}
