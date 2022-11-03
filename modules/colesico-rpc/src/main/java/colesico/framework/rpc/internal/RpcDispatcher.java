package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.Rpc;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcLigature;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleMethod;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
public class RpcDispatcher {

    protected final Logger logger = LoggerFactory.getLogger(RpcDispatcher.class);

    /**
     * RPC API name to tele-methods map
     */
    protected final Map<String, Map<String, TeleMethod>> rpcApiMap = new HashMap<>();

    public RpcDispatcher(@Classed(Rpc.class) Polysupplier<TeleFacade> teleFacadesSupp) {
        loadLigature(teleFacadesSupp);
    }

    public void dispatch(RpcExchange exchange) {

        TeleMethod teleMethod = resolveTeleMethod(exchange);

        if (teleMethod != null) {
            // Invoke
            teleMethod.invoke();
        }
    }

    /**
     * Resolve tele method
     *
     * @return tele method or null if mappings not found
     */
    protected TeleMethod resolveTeleMethod(RpcExchange exchange) {
        try {
            RpcExchange.Operation operation = exchange.resolveOperation();
            logger.debug("RPC operation: {}", operation);

            Map<String, TeleMethod> apiMethods = rpcApiMap.get(operation.apiName());
            if (apiMethods == null) {
                String errMsg = "RPC API not found: " + operation.apiName();
                logger.error(errMsg);
                exchange.sendError(RpcError.of(errMsg));
                return null;
            }

            TeleMethod teleMethod = apiMethods.get(operation.methodName());
            if (teleMethod == null) {
                String errMsg = "RPC method not found: " + operation.methodName();
                logger.error(errMsg);
                exchange.sendError(RpcError.of(errMsg));
                return null;
            }

            return teleMethod;
        } catch (Throwable t) {
            String errMsg = "RPC resolve operation error: " + ExceptionUtils.getRootCauseMessage(t);
            logger.error(errMsg);
            exchange.sendError(RpcError.of(t.getClass(), errMsg));
            return null;
        }
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
                Map<String, TeleMethod> prevMethods = rpcApiMap.put(rpcApi.getRpcName(), rpcApi.getTeleMethods());
                if (prevMethods != null) {
                    throw new RpcException("Duplicate RPC API implementation: " + rpcApi.getRpcName());
                }
            }
        }
    }
}
