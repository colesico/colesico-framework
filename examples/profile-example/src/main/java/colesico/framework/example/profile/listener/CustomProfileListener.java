package colesico.framework.example.profile.listener;

import colesico.framework.example.profile.custom.CustomProfile;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileListener;

public class CustomProfileListener implements ProfileListener<CustomProfile> {


    @Override
    public Profile beforeWrite(CustomProfile profile) {
        return profile;
    }

    @Override
    public Profile afterRead(CustomProfile profile) {
        profile.setApiVersion("2.0");
        return profile;
    }
}
