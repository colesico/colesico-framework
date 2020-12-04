package colesico.framework.rpc.teleapi.writer;

import colesico.framework.rpc.teleapi.BasicEnvelope;
import colesico.framework.rpc.teleapi.RpcTWContext;
import colesico.framework.rpc.teleapi.RpcTeleWriter;
import colesico.framework.security.Principal;
import colesico.framework.security.teleapi.PrincipalSerializer;

import javax.inject.Singleton;

@Singleton
public class RpcPrincipalWriter implements RpcTeleWriter<Principal> {

    protected final PrincipalSerializer principalSerializer;

    public RpcPrincipalWriter(PrincipalSerializer principalSerializer) {
        this.principalSerializer = principalSerializer;
    }

    @Override
    public void write(Principal value, RpcTWContext context) {
        BasicEnvelope env = (BasicEnvelope) context.getResponse();
        env.setPrincipal(principalSerializer.serialize(value));
    }
}
