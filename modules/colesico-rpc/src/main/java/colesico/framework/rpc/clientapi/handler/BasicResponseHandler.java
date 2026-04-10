package colesico.framework.rpc.clientapi.handler;

import colesico.framework.profile.ProfileContext;
import colesico.framework.rpc.clientapi.RpcResponseHandler;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.security.Principal;
import colesico.framework.security.SecurityContext;
import colesico.framework.security.teleapi.PrincipalSerializer;
import jakarta.inject.Singleton;

@Singleton
public class BasicResponseHandler implements RpcResponseHandler<BasicEnvelope> {


    private final ProfileContext profileContext;
    private final ProfileContext profileUtils;
    private final SecurityContext securityContext;
    private final PrincipalSerializer principalSerializer;

    public BasicResponseHandler(ProfileContext profileContext, ProfileContext profileUtils, SecurityContext securityContext, PrincipalSerializer principalSerializer) {
        this.profileContext = profileContext;
        this.profileUtils = profileUtils;
        this.securityContext = securityContext;
        this.principalSerializer = principalSerializer;
    }

    @Override
    public void onResponse(BasicEnvelope response) {
        if (response.getPrincipal() != null) {
            if (response.getPrincipal().length == 0) {
                securityContext.setPrincipal(null);
            } else {
                Principal principal = principalSerializer.deserialize(response.getPrincipal());
                securityContext.setPrincipal(principal);
            }
        }

        if (response.getProfile() != null) {
            if (response.getProfile().length != 0) {
                // Response contains profile preferences only
                var profile = profileUtils.deserialize(response.getProfile());
                profileContext.commit(profile);
            }
        }
    }
}
