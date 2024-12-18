package colesico.framework.rpc.teleapi.reader;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTRContext;
import colesico.framework.rpc.teleapi.RpcTeleReader;

import javax.inject.Singleton;
import java.util.Locale;

@Singleton
public class RpcProfileReader implements RpcTeleReader<Profile> {

    protected final ProfileUtils profileUtils;

    public RpcProfileReader(ProfileUtils profileUtils) {
        this.profileUtils = profileUtils;
    }

    @Override
    public Profile read(RpcTRContext context) {
        Profile profile = null;
        if (context.getValueGetter() != null) {
            profile = (Profile) context.getValueGetter().get(context.getRequest());
        } else {
            BasicEnvelope env = (BasicEnvelope) context.getRequest();
            profile = profileUtils.deserialize(env.getProfile());
        }

        return profile;
    }
}
