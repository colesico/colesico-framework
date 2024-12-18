package colesico.framework.rpc.teleapi.writer;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTWContext;
import colesico.framework.rpc.teleapi.RpcTeleWriter;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class RpcProfileWriter implements RpcTeleWriter<Profile> {

    protected final ProfileUtils profileUtils;

    public RpcProfileWriter(ProfileUtils profileUtils) {
        this.profileUtils = profileUtils;
    }

    @Override
    public void write(Profile value, RpcTWContext context) {
        BasicEnvelope env = (BasicEnvelope) context.getResponse();
        if (value != null) {
            Collection preferences = profileUtils.getPreferences(value);
            env.setProfile(profileUtils.toBytes(preferences));
        } else {
            env.setProfile(new byte[0]);
        }
    }
}
