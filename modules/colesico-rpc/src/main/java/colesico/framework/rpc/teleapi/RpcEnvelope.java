package colesico.framework.rpc.teleapi;

import colesico.framework.profile.Profile;
import colesico.framework.security.Principal;

/**
 * Default envelope extension
 */
public interface RpcEnvelope {

    Principal getPrincipal();

    void setPrincipal(Principal principal);

    Profile getProfile();

    void setProfile(Profile profile);
}
