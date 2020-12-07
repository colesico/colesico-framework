package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.clientapi.RpcErrorHandler;
import colesico.framework.rpc.clientapi.RpcErrorHandlerFactory;
import colesico.framework.rpc.clientapi.handler.BasicRequestHandler;
import colesico.framework.rpc.clientapi.handler.BasicRpcErrorHandler;
import colesico.framework.rpc.rpcgear.kryo.KryoClient;
import colesico.framework.rpc.rpcgear.kryo.KryoExchange;
import colesico.framework.rpc.rpcgear.kryo.KryoSerializer;
import colesico.framework.rpc.internal.router.RpcDispatcher;
import colesico.framework.rpc.internal.teleapi.RpcTeleDriverImpl;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.rpc.teleapi.RpcTeleReader;
import colesico.framework.rpc.teleapi.RpcTeleWriter;
import colesico.framework.rpc.clientapi.RpcRequestHandler;
import colesico.framework.rpc.clientapi.RpcClient;
import colesico.framework.rpc.teleapi.reader.RpcPrincipalReader;
import colesico.framework.rpc.teleapi.reader.RpcProfileReader;
import colesico.framework.rpc.teleapi.writer.RpcPrincipalWriter;
import colesico.framework.rpc.teleapi.writer.RpcProfileWriter;
import colesico.framework.security.Principal;

import javax.inject.Singleton;

@Producer
@Produce(RpcErrorHandlerFactory.class)
@Produce(RpcDispatcher.class)
@Produce(RpcTeleDriverImpl.class)

@Produce(KryoSerializer.class)
@Produce(KryoClient.class)
@Produce(KryoExchange.class)

@Produce(RpcPrincipalReader.class)
@Produce(RpcPrincipalWriter.class)

@Produce(RpcProfileReader.class)
@Produce(RpcProfileWriter.class)

@Produce(value = BasicRequestHandler.class, supertypes = RpcRequestHandler.class, polyproduce = true)
@Produce(BasicRpcErrorHandler.class)
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

    // Readers

    @Singleton
    @Classed(Principal.class)
    public RpcTeleReader getPrincipalReader(RpcPrincipalReader impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public RpcTeleReader getProfileReader(RpcProfileReader impl) {
        return impl;
    }

    // Writers

    @Singleton
    @Classed(Principal.class)
    public RpcTeleWriter getPrincipalWriter(RpcPrincipalWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public RpcTeleWriter getProfileWriter(RpcProfileWriter impl) {
        return impl;
    }

    // Error handlers

    @Singleton
    @Classed(RpcError.class)
    public RpcErrorHandler getRpcErrorHandler(BasicRpcErrorHandler impl) {
        return impl;
    }
}
