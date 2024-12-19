package colesico.framework.rpc.teleapi.writer;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTWContext;
import colesico.framework.rpc.teleapi.RpcTeleWriter;

import javax.inject.Singleton;

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
            env.setProfile(profileUtils.toBytes(value.getPreferences()));
        } else {
            env.setProfile(new byte[0]);
        }
    }
}
