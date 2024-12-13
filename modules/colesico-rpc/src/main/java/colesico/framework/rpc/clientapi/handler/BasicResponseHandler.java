package colesico.framework.rpc.clientapi.handler;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileListener;
import colesico.framework.profile.ProfileKit;
import colesico.framework.rpc.clientapi.RpcResponseHandler;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.security.Principal;
import colesico.framework.security.SecurityKit;
import colesico.framework.security.teleapi.PrincipalSerializer;

import javax.inject.Singleton;

@Singleton
public class BasicResponseHandler implements RpcResponseHandler<BasicEnvelope> {

    private final PrincipalSerializer principalSerializer;
    private final ProfileListener profileSerializer;
    private final SecurityKit securityKit;
    private final ProfileKit profileKit;

    public BasicResponseHandler(PrincipalSerializer principalSerializer, ProfileListener profileSerializer, SecurityKit securityKit, ProfileKit profileKit) {
        this.principalSerializer = principalSerializer;
        this.profileSerializer = profileSerializer;
        this.securityKit = securityKit;
        this.profileKit = profileKit;
    }

    @Override
    public void onResponse(BasicEnvelope response) {
        if (response.getPrincipal() != null) {
            if (response.getPrincipal().length == 0) {
                securityKit.setPrincipal(null);
            } else {
                Principal principal = principalSerializer.deserialize(response.getPrincipal());
                securityKit.setPrincipal(principal);
            }
        }

        if (response.getProfile() != null) {
            if (response.getProfile().length == 0) {
                profileKit.setProfile(null);
            } else {
                Profile profile = profileSerializer.deserialize(response.getProfile());
                profileKit.setProfile(profile);
            }
        }
    }
}
