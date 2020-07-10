package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.rpc.teleapi.reader.PrincipalReader;
import colesico.framework.rpc.teleapi.reader.PrincipalWriter;
import colesico.framework.rpc.teleapi.reader.RpcTeleReader;
import colesico.framework.rpc.teleapi.reader.RpcTeleWriter;
import colesico.framework.security.Principal;
import colesico.framework.teleapi.DataPort;

import javax.inject.Singleton;

@Producer
@Produce(RpcController.class)
@Produce(RpcTeleDriverImpl.class)
@Produce(KryoExchange.class)

@Produce(PrincipalReader.class)
@Produce(PrincipalWriter.class)
public class RpcProducer {

    @Singleton
    public RpcTeleDriver getRpcTeleDriver(RpcTeleDriverImpl impl) {
        return impl;
    }

    @Unscoped
    public RpcDataPort getRpcDataPort(ThreadScope threadScope) {
        return (RpcDataPort) threadScope.get(DataPort.SCOPE_KEY);
    }

    @Singleton
    public RpcExchange getDefaultRpcExchange(KryoExchange impl) {
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
