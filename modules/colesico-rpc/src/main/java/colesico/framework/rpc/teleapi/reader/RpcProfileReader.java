package colesico.framework.rpc.teleapi.reader;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileListener;
import colesico.framework.profile.teleapi.CommonProfileCreator;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTRContext;
import colesico.framework.rpc.teleapi.RpcTeleReader;

import javax.inject.Singleton;
import java.util.Locale;

@Singleton
public class RpcProfileReader implements RpcTeleReader<Profile> {

    protected final ProfileListener profileSerializer;
    protected final CommonProfileCreator commonProfileCreator;

    public RpcProfileReader(ProfileListener profileSerializer, CommonProfileCreator commonProfileCreator) {
        this.profileSerializer = profileSerializer;
        this.commonProfileCreator = commonProfileCreator;
    }

    @Override
    public Profile read(RpcTRContext context) {
        Profile profile;
        if (context.getValueGetter() != null) {
            profile = (Profile) context.getValueGetter().get(context.getRequest());
        } else {
            BasicEnvelope env = (BasicEnvelope) context.getRequest();
            profile = profileSerializer.deserialize(env.getProfile());
        }
        if (profile == null) {
            profile = commonProfileCreator.createCommonProfile(Locale.getDefault());
        }
        return profile;
    }
}
