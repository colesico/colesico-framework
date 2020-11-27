package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.rpc.internal.kryo.KryoClient;
import colesico.framework.rpc.internal.kryo.KryoExchange;
import colesico.framework.rpc.internal.kryo.KryoSerializer;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.rpc.teleapi.client.RpcClient;

import javax.inject.Singleton;

@Producer
@Produce(RpcDispatcher.class)
@Produce(RpcTeleDriverImpl.class)

@Produce(KryoSerializer.class)
@Produce(KryoClient.class)
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

    @Singleton
    public RpcClient getDefaultRpcClient(KryoClient impl) {
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
