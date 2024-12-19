package colesico.framework.rpc.clientapi.handler;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileSource;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.rpc.clientapi.RpcResponseHandler;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.security.Principal;
import colesico.framework.security.SecurityKit;
import colesico.framework.security.teleapi.PrincipalSerializer;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class BasicResponseHandler implements RpcResponseHandler<BasicEnvelope> {


    private final ProfileSource profileSource;
    private final ProfileUtils profileUtils;
    private final SecurityKit securityKit;
    private final PrincipalSerializer principalSerializer;

    public BasicResponseHandler(ProfileSource profileSource, ProfileUtils profileUtils, SecurityKit securityKit, PrincipalSerializer principalSerializer) {
        this.profileSource = profileSource;
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
            if (response.getProfile().length == 0) {
                profileSource.write(null);
            } else {
                Collection prefs = profileUtils.fromBytes(response.getProfile());
                Profile profile = profileUtils.fromPreferences(prefs);
                profileSource.write(profile);
            }
        }
    }
}
