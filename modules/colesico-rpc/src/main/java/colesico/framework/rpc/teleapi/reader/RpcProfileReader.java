package colesico.framework.rpc.teleapi.reader;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileManager;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTRContext;
import colesico.framework.rpc.teleapi.RpcTeleReader;

import jakarta.inject.Singleton;

@Singleton
public class RpcProfileReader implements RpcTeleReader<Profile> {

    protected final ProfileManager profileManager;

    public RpcProfileReader(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public Profile read(RpcTRContext context) {
        Profile profile = null;
        if (context.getValueGetter() != null) {
            profile = (Profile) context.getValueGetter().get(context.getRequest());
        } else {
            BasicEnvelope env = (BasicEnvelope) context.getRequest();
            //profile = profileUtils.deserialize(env.getProfile());
        }

        return profile;
    }
}
