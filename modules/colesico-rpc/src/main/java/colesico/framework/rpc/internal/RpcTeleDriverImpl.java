package colesico.framework.rpc.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.MethodInvoker;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class RpcTeleDriverImpl implements RpcTeleDriver {

    private final Ioc ioc;
    private final RpcExchange exchange;

    /**
     * To store data-port ref.
     */
    protected final ThreadScope threadScope;

    @Inject
    public RpcTeleDriverImpl(Ioc ioc, RpcExchange exchange, ThreadScope threadScope) {
        this.ioc = ioc;
        this.exchange = exchange;
        this.threadScope = threadScope;
    }

    @Override
    public <T> void invoke(T service, MethodInvoker<T, RpcDataPort> invoker, RpcTIContext invCtx) {

        RpcRequest request = null;
        RpcResponse response = null;
        RpcDataPort dataPort = null;
        try {
            // Create request and response
            request = exchange.readRequest(invCtx.getRequestType());
            response = invCtx.getResponseType().getDeclaredConstructor().newInstance();

            // Instantiate data port
            dataPort = new RpcDataPortImpl(ioc, request, response);
            threadScope.put(DataPort.SCOPE_KEY, dataPort);

            // Invoke target service method
            invoker.invoke(service, dataPort);
        } catch (Throwable t) {
            dataPort.writeError(t);
        } finally {
            exchange.writeResponse(response);
        }
    }

}
