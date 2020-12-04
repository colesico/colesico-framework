package colesico.framework.rpc.teleapi.reader;

import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.ProfileSerializer;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTRContext;
import colesico.framework.rpc.teleapi.RpcTeleReader;

import javax.inject.Singleton;

@Singleton
public class RpcProfileReader implements RpcTeleReader<Profile> {

    protected final ProfileSerializer profileSerializer;

    public RpcProfileReader(ProfileSerializer profileSerializer) {
        this.profileSerializer = profileSerializer;
    }

    @Override
    public Profile read(RpcTRContext context) {
        BasicEnvelope env = (BasicEnvelope) context.getRequest();
        Profile profile = profileSerializer.deserialize(env.getProfile());
        return profile;
    }
}
