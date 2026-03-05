package colesico.framework.rpc.teleapi.writer;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileManager;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTWContext;
import colesico.framework.rpc.teleapi.RpcTeleWriter;

import jakarta.inject.Singleton;

@Singleton
public class RpcProfileWriter implements RpcTeleWriter<Profile> {

    protected final ProfileManager profileManager;

    public RpcProfileWriter(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public void write(Profile value, RpcTWContext context) {
        BasicEnvelope env = (BasicEnvelope) context.getResponse();
        if (value != null) {
         //   env.setProfile(profileUtils.toBytes(profileUtils.getPreferences(value)));
        } else {
            env.setProfile(new byte[0]);
        }
    }
}
