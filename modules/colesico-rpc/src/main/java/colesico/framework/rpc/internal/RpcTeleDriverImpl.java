package colesico.framework.rpc.internal;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.MethodInvoker;
import colesico.framework.teleapi.TeleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RpcTeleDriverImpl implements RpcTeleDriver {

    private static final Logger logger = LoggerFactory.getLogger(RpcTeleDriverImpl.class);

    private final TeleFactory teleFactory;
    private final RpcExchange exchange;

    /**
     * To store data-port ref.
     */
    protected final ThreadScope threadScope;

    @Inject
    public RpcTeleDriverImpl(TeleFactory ioc, RpcExchange exchange, ThreadScope threadScope) {
        this.teleFactory = ioc;
        this.exchange = exchange;
        this.threadScope = threadScope;
    }

    @Override
    public <T> void invoke(T service, MethodInvoker<T, RpcDataPort> invoker, RpcTIContext invCtx) {

        // Create request and response
        RpcRequest request = exchange.readRequest(invCtx.getRequestType());
        RpcResponse response = createResponse(invCtx.getResponseType());

        // Instantiate data port
        RpcDataPort dataPort = new RpcDataPortImpl(teleFactory, request, response);
        threadScope.put(DataPort.SCOPE_KEY, dataPort);

        try {
            // Invoke target service method
            invoker.invoke(service, dataPort);
        } catch (Throwable t) {
            logger.error("Error invoking rpc method: {}", service.getClass());
            dataPort.writeError(t);
        } finally {
            exchange.writeResponse(response);
        }
    }

    private RpcResponse createResponse(Class<? extends RpcResponse> respClass) {
        try {
            return respClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
