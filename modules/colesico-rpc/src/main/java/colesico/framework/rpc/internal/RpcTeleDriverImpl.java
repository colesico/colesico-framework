package colesico.framework.rpc.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.rpc.Error;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcTIContext;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.teleapi.DataPort;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;

public class RpcTeleDriverImpl implements RpcTeleDriver {

    protected final ThreadScope threadScope;
    protected final RpcDataPort dataPort;

    public RpcTeleDriverImpl(ThreadScope threadScope, RpcDataPort dataPort) {
        this.threadScope = threadScope;
        this.dataPort = dataPort;
    }

    @Override
    public <T> void invoke(T service, Binder<T, RpcDataPort> binder, RpcTIContext invCtx) {
        threadScope.put(DataPort.SCOPE_KEY, dataPort);
        binder.invoke(service, dataPort);
    }

}
