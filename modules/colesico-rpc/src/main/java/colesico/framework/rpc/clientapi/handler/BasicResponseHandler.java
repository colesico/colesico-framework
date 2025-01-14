package colesico.framework.rpc.clientapi.handler;

import colesico.framework.profile.ProfileKit;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.rpc.clientapi.RpcResponseHandler;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.security.Principal;
import colesico.framework.security.SecurityKit;
import colesico.framework.security.teleapi.PrincipalSerializer;
import jakarta.inject.Singleton;

@Singleton
public class BasicResponseHandler implements RpcResponseHandler<BasicEnvelope> {


    private final ProfileKit profileKit;
    private final ProfileUtils profileUtils;
    private final SecurityKit securityKit;
    private final PrincipalSerializer principalSerializer;

    public BasicResponseHandler(ProfileKit profileKit, ProfileUtils profileUtils, SecurityKit securityKit, PrincipalSerializer principalSerializer) {
        this.profileKit = profileKit;
        this.profileUtils = profileUtils;
        this.securityKit = securityKit;
        this.principalSerializer = principalSerializer;
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
            if (response.getProfile().length != 0) {
                // Response contains profile preferences only
                var profile = profileUtils.deserialize(response.getProfile());
                profileKit.commit(profile);
            }
        }
    }
}
