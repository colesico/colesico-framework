package colesico.framework.rpc.teleapi.writer;

import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.ProfileSerializer;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTWContext;
import colesico.framework.rpc.teleapi.RpcTeleWriter;

import javax.inject.Singleton;

@Singleton
public class RpcProfileWriter  implements RpcTeleWriter<Profile> {

    protected final ProfileSerializer profileSerializer;

    public RpcProfileWriter(ProfileSerializer profileSerializer) {
        this.profileSerializer = profileSerializer;
    }

    @Override
    public void write(Profile value, RpcTWContext context) {
        BasicEnvelope env = (BasicEnvelope) context.getResponse();
        env.setProfile(profileSerializer.serialize(value));
    }
}
