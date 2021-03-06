package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.clientapi.*;
import colesico.framework.rpc.clientapi.handler.ApplicationErrorHandler;
import colesico.framework.rpc.clientapi.handler.BasicRequestHandler;
import colesico.framework.rpc.clientapi.handler.BasicResponseHandler;
import colesico.framework.rpc.clientapi.handler.BasicRpcErrorHandler;
import colesico.framework.rpc.rpcgear.kryo.KryoClient;
import colesico.framework.rpc.rpcgear.kryo.KryoExchange;
import colesico.framework.rpc.rpcgear.kryo.KryoSerializer;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.rpc.teleapi.RpcTeleReader;
import colesico.framework.rpc.teleapi.RpcTeleWriter;
import colesico.framework.rpc.teleapi.reader.RpcPrincipalReader;
import colesico.framework.rpc.teleapi.reader.RpcProfileReader;
import colesico.framework.rpc.teleapi.writer.ApplicationExceptionWriter;
import colesico.framework.rpc.teleapi.writer.RpcPrincipalWriter;
import colesico.framework.rpc.teleapi.writer.RpcProfileWriter;
import colesico.framework.security.Principal;
import colesico.framework.service.ApplicationException;

import javax.inject.Singleton;

@Producer
@Produce(RpcErrorHandlerFactory.class)
@Produce(RpcDispatcher.class)
@Produce(RpcTeleDriverImpl.class)

// Readers and writers
@Produce(RpcPrincipalReader.class)
@Produce(RpcPrincipalWriter.class)

@Produce(RpcProfileReader.class)
@Produce(RpcProfileWriter.class)

@Produce(ApplicationExceptionWriter.class)

// Request/Response/Error handlers
@Produce(value = BasicRequestHandler.class, keyType = RpcRequestHandler.class, polyproduce = true)
@Produce(value = BasicResponseHandler.class, keyType = RpcResponseHandler.class, polyproduce = true)
@Produce(BasicRpcErrorHandler.class)
@Produce(ApplicationErrorHandler.class)

// Kryo gear
@Produce(KryoSerializer.class)
@Produce(KryoClient.class)
@Produce(KryoExchange.class)

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

    @Singleton
    @Classed(ApplicationException.class)
    public RpcTeleWriter getApplicationExceptionWriter(ApplicationExceptionWriter impl) {
        return impl;
    }

    // Error handlers

    @Singleton
    @Classed(RpcError.class)
    public RpcErrorHandler getRpcErrorHandler(BasicRpcErrorHandler impl) {
        return impl;
    }

    @Singleton
    @Classed(ApplicationExceptionWriter.ApplicationError.class)
    public RpcErrorHandler getApplicationErrorHandler(ApplicationErrorHandler impl) {
        return impl;
    }

}
