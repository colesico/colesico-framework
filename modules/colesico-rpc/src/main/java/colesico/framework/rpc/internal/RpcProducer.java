package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.rpc.kryo.KryoExchange;
import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.teleapi.DataPort;

import javax.inject.Singleton;

@Producer
@Produce(RpcDispatcher.class)
@Produce(RpcTeleDriverImpl.class)
@Produce(KryoExchange.class)

//@Produce(PrincipalReader.class)
//@Produce(PrincipalWriter.class)
public class RpcProducer {

    @Singleton
    public RpcTeleDriver getRpcTeleDriver(RpcTeleDriverImpl impl) {
        return impl;
    }

    @Singleton
    public RpcExchange getDefaultRpcExchange(KryoExchange impl) {
        return impl;
    }

    /*
    @Singleton
    @Classed(Principal.class)
    public RpcTeleReader getPrincipalReader(PrincipalReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Principal.class)
    public RpcTeleWriter getPrincipalWriter(PrincipalWriter impl) {
        return impl;
    }
*/
}
