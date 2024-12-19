package colesico.framework.rpc.clientapi.handler;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.rpc.clientapi.RpcRequestHandler;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.security.Principal;
import colesico.framework.security.teleapi.PrincipalSerializer;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class BasicRequestHandler implements RpcRequestHandler<BasicEnvelope> {

    private final PrincipalSerializer principalSerializer;
    private final ProfileUtils profileUtils;

    private final Provider<Principal> principalProv;
    private final Provider<Profile> profileProv;

    public BasicRequestHandler(PrincipalSerializer principalSerializer, ProfileUtils profileUtils, Provider<Principal> principalProv, Provider<Profile> profileProv) {
        this.principalSerializer = principalSerializer;
        this.profileUtils = profileUtils;
        this.principalProv = principalProv;
        this.profileProv = profileProv;
    }

    @Override
    public void onRequest(BasicEnvelope request) {
        Principal principal = principalProv.get();
        if (principal != null) {
            request.setPrincipal(principalSerializer.serialize(principal));
        } else {
            request.setPrincipal(null);
        }

        Profile profile = profileProv.get();
        if (profile != null) {
            request.setProfile(profileUtils.serialize(profile));
        } else {
            request.setProfile(null);
        }
    }
}
