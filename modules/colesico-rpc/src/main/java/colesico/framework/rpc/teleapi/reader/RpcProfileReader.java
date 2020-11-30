package colesico.framework.rpc.teleapi.reader;

import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.ProfileSerializer;
import colesico.framework.rpc.teleapi.RpcEnvelope;
import colesico.framework.rpc.teleapi.RpcTRContext;
import colesico.framework.rpc.teleapi.RpcTeleReader;

public class RpcProfileReader implements RpcTeleReader<Profile> {

    protected final ProfileSerializer profileSerializer;

    public RpcProfileReader(ProfileSerializer profileSerializer) {
        this.profileSerializer = profileSerializer;
    }

    @Override
    public Profile read(RpcTRContext context) {
        RpcEnvelope env = (RpcEnvelope) context.getRequest();
        Profile profile = profileSerializer.deserialize(env.getProfile());
        return profile;
    }
}
