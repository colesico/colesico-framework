package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcTeleDriver;

import javax.inject.Singleton;

@Producer
@Produce(RpcDataProtImpl.class)
@Produce(RpcTeleDriverImpl.class)
public class RpcProducer {

    @Singleton
    public RpcDataPort getRpcDataPort(RpcDataProtImpl impl) {
        return impl;
    }

    @Singleton
    public RpcTeleDriver getRpcTeleDriver(RpcTeleDriverImpl impl) {
        return impl;
    }
}
