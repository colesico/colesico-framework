package colesico.framework.rpc.teleapi.reader;

import colesico.framework.rpc.teleapi.RpcEnvelope;
import colesico.framework.rpc.teleapi.RpcTRContext;
import colesico.framework.rpc.teleapi.RpcTeleReader;
import colesico.framework.security.Principal;
import colesico.framework.security.teleapi.PrincipalSerializer;

public class RpcPrincipalReader implements RpcTeleReader<Principal> {

    protected final PrincipalSerializer principalSerializer;

    public RpcPrincipalReader(PrincipalSerializer principalSerializer) {
        this.principalSerializer = principalSerializer;
    }

    @Override
    public Principal read(RpcTRContext context) {
        RpcEnvelope env = (RpcEnvelope) context.getRequest();
        Principal principal = principalSerializer.deserialize(env.getPrincipal());
        return principal;
    }
}
