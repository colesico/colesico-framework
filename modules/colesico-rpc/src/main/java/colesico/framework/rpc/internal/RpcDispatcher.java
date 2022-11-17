package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.RpcError;
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

    protected final Map<String, RpcNamespace> rpcNamespaces = new HashMap<>();

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

            RpcNamespace rpcNamespace = rpcNamespaces.get(operation.rpcNamespace());
            if (rpcNamespace == null) {
                String errMsg = "RPC Namespace not found: " + operation.rpcNamespace();
                logger.error(errMsg);
                exchange.sendError(RpcError.of(errMsg));
                return null;
            }

            Map<String, TeleMethod> apiMethods = rpcNamespace.getApiMethods(operation.rpcApiName());
            if (apiMethods == null) {
                String errMsg = "RPC API not found: " + operation.rpcApiName();
                logger.error(errMsg);
                exchange.sendError(RpcError.of(errMsg));
                return null;
            }

            TeleMethod teleMethod = apiMethods.get(operation.rpcMethodName());
            if (teleMethod == null) {
                String errMsg = "RPC method not found: " + operation.rpcMethodName();
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

            List<RpcLigature.RpcApiSpec> rpcApiList = ligature.getRpcApiList();
            for (RpcLigature.RpcApiSpec rpcApi : rpcApiList) {
                RpcNamespace rpcNamespace = rpcNamespaces.computeIfAbsent(rpcApi.getRpcNamespace(), k -> new RpcNamespace());
                rpcNamespace.addMethods(rpcApi.getRpcName(), rpcApi.getRpcMethods());
            }
        }
    }

    public static final class RpcNamespace {
        /**
         * RPC API name to tele-methods map
         */
        protected final Map<String, Map<String, TeleMethod>> rpcApiMap = new HashMap<>();

        public Map<String, TeleMethod> getApiMethods(String rpcApiName) {
            return rpcApiMap.get(rpcApiName);
        }

        public void addMethods(String rpcApiName, Map<String, TeleMethod> rpcMethods) {
            Map<String, TeleMethod> allMethods = rpcApiMap.computeIfAbsent(rpcApiName, k -> new HashMap<>());
            allMethods.putAll(rpcMethods);
        }
    }
}
