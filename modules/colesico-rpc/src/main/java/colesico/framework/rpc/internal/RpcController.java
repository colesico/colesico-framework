package colesico.framework.rpc.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * RPC request processing controller
 */
@Singleton
public class RpcController {

    protected final Logger logger = LoggerFactory.getLogger(RpcController.class);

    /**
     * To extract accurate tele-readers/writers
     */
    protected final Ioc ioc;

    /**
     * To store data-port ref.
     */
    protected final ThreadScope threadScope;

    /**
     * RPC Interface names to RPC API ref map
     * RPC API name to RPC API ligature
     */
    protected final Map<String, RpcLigature.RpcApi> rpcApiMap = new HashMap<>();

    public RpcController(@Classed(Rpc.class) Polysupplier<TeleFacade> teleFacadesSupp,
                         Ioc ioc,
                         ThreadScope threadScope) {

        this.ioc = ioc;
        this.threadScope = threadScope;
        loadLigature(teleFacadesSupp);

    }

    public void dispatch(RpcExchange exchange) {

        RpcRequest request = null;
        RpcResponse response = null;
        RpcDataPort dataPort = null;

        try {
            // Resolve RPC method
            RpcExchange.RequestResolution requestResolution = exchange.resolveRequest();
            RpcLigature.RpcMethod rpcMethod = findRpcMethod(requestResolution);

            // Create request and response
            request = exchange.readRequest(rpcMethod.getRequestType());
            response = rpcMethod.getResponseType().getDeclaredConstructor().newInstance();

            // Instantiate data port
            dataPort = new RpcDataPortImpl(ioc, request, response);
            threadScope.put(DataPort.SCOPE_KEY, dataPort);

            // Invoke target method
            rpcMethod.getTeleMethod().invoke();
        } catch (Exception e) {
            if (dataPort == null) {
                response = new RpcResponse() {
                };
                dataPort = new RpcDataPortImpl(ioc, null, response);
            }
            dataPort.writeError(e);
        } finally {
            exchange.writeResponse(response);
        }
    }

    protected RpcLigature.RpcMethod findRpcMethod(RpcExchange.RequestResolution requestResolution) {
        RpcLigature.RpcApi rpcApi = rpcApiMap.get(requestResolution.getApiName());
        if (rpcApi == null) {
            throw new RpcException("RPC API not found: " + requestResolution.getApiName());
        }

        RpcLigature.RpcMethod rpcMethod = rpcApi.getTargetMethods().get(requestResolution.getMethodName());
        if (rpcMethod == null) {
            throw new RpcException("RPC method not found: " + requestResolution.getApiName());
        }

        return rpcMethod;
    }

    protected void loadLigature(Polysupplier<TeleFacade> teleFacadeSupp) {
        logger.debug("Lookup RPC tele-facades... ");

        Iterator<TeleFacade> it = teleFacadeSupp.iterator(null);

        while (it.hasNext()) {
            TeleFacade teleFacade = it.next();
            logger.debug("Found RPC tele-facade: {}", teleFacade.getClass().getName());
            RpcLigature ligature = (RpcLigature) teleFacade.getLigature();

            List<RpcLigature.RpcApi> rpcApiList = ligature.getRpcApiList();
            for (RpcLigature.RpcApi rpcApi : rpcApiList) {
                RpcLigature.RpcApi prevApi = rpcApiMap.put(rpcApi.getName(), rpcApi);
                if (prevApi != null) {
                    throw new RpcException("Duplicate RPC API implementation: " + rpcApi.getName());
                }
            }
        }
    }
}
