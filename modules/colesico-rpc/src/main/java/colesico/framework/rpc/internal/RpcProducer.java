package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcDispatcher;
import colesico.framework.rpc.teleapi.RpcSerializer;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.rpc.teleapi.reader.PrincipalReader;
import colesico.framework.rpc.teleapi.reader.PrincipalWriter;
import colesico.framework.rpc.teleapi.reader.RpcTeleReader;
import colesico.framework.rpc.teleapi.reader.RpcTeleWriter;
import colesico.framework.security.Principal;

import javax.inject.Singleton;

@Producer
@Produce(RpcDataProtImpl.class)
@Produce(RpcTeleDriverImpl.class)
@Produce(RpcDispatcherImpl.class)
@Produce(RpcHttpDispatcher.class)
@Produce(KryoSerializer.class)

@Produce(PrincipalReader.class)
@Produce(PrincipalWriter.class)
public class RpcProducer {

    @Singleton
    public RpcDataPort getRpcDataPort(RpcDataProtImpl impl) {
        return impl;
    }

    @Singleton
    public RpcTeleDriver getRpcTeleDriver(RpcTeleDriverImpl impl) {
        return impl;
    }

    @Singleton
    public RpcDispatcher getRpcTeleDriver(RpcDispatcherImpl impl) {
        return impl;
    }

    @Singleton
    public RpcSerializer getDefaultSerializer(KryoSerializer impl) {
        return impl;
    }

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

}
