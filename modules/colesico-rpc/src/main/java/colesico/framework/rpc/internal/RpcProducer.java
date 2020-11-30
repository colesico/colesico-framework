package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.rpc.internal.kryo.KryoClient;
import colesico.framework.rpc.internal.kryo.KryoExchange;
import colesico.framework.rpc.internal.kryo.KryoSerializer;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.rpc.teleapi.RpcTeleReader;
import colesico.framework.rpc.teleapi.RpcTeleWriter;
import colesico.framework.rpc.teleapi.client.RpcClient;
import colesico.framework.rpc.teleapi.reader.RpcPrincipalReader;
import colesico.framework.rpc.teleapi.reader.RpcProfileReader;
import colesico.framework.rpc.teleapi.writer.RpcPrincipalWriter;
import colesico.framework.rpc.teleapi.writer.RpcProfileWriter;
import colesico.framework.security.Principal;

import javax.inject.Singleton;

@Producer
@Produce(RpcDispatcher.class)
@Produce(RpcTeleDriverImpl.class)

@Produce(KryoSerializer.class)
@Produce(KryoClient.class)
@Produce(KryoExchange.class)

@Produce(RpcPrincipalReader.class)
@Produce(RpcPrincipalWriter.class)

@Produce(RpcProfileReader.class)
@Produce(RpcProfileWriter.class)
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

    // Readers and writers
    
    @Singleton
    @Classed(Principal.class)
    public RpcTeleReader getPrincipalReader(RpcPrincipalReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Principal.class)
    public RpcTeleWriter getPrincipalWriter(RpcPrincipalWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public RpcTeleReader getProfileReader(RpcProfileReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public RpcTeleWriter getProfileWriter(RpcProfileWriter impl) {
        return impl;
    }

}
