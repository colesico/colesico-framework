package colesico.framework.rpc.clientapi.handler;

import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.ProfileSerializer;
import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.clientapi.RpcRequestHandler;
import colesico.framework.security.Principal;
import colesico.framework.security.teleapi.PrincipalSerializer;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class BasicRequestHandler implements RpcRequestHandler<BasicEnvelope> {

    private final PrincipalSerializer principalSerializer;
    private final ProfileSerializer profileSerializer;

    private final Provider<Principal> principalProv;
    private final Provider<Profile> profileProv;

    public BasicRequestHandler(PrincipalSerializer principalSerializer, ProfileSerializer profileSerializer, Provider<Principal> principalProv, Provider<Profile> profileProv) {
        this.principalSerializer = principalSerializer;
        this.profileSerializer = profileSerializer;
        this.principalProv = principalProv;
        this.profileProv = profileProv;
    }

    @Override
    public void onRequest(BasicEnvelope request) {
        Principal principal = principalProv.get();
        request.setPrincipal(principalSerializer.serialize(principal));

        Profile profile = profileProv.get();
        request.setProfile(profileSerializer.serialize(profile));
    }
}
