package colesico.framework.security.teleapi;

import colesico.framework.security.Principal;

public interface PrincipalTeleAssist<P extends Principal> {

    byte[] serialize(P principal);

    P deserialize(byte[] principalBytes);
}
