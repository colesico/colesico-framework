package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
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
     * RPC Interface names to RPC API ref map
     */
    protected final Map<String, RpcLigature.RpcApi> rpcApiMap = new HashMap<>();

    public RpcDispatcher(@Classed(Rpc.class) Polysupplier<TeleFacade> teleFacadesSupp) {
        loadLigature(teleFacadesSupp);
    }

    public void dispatch(RpcExchange exchange) {
        try {
            // Resolve RPC tele-method method
            RpcExchange.RequestResolution resolution = exchange.resolveRequest();
            logger.debug("RPC request resolution: {}", resolution);
            TeleMethod teleMethod = getTeleMethod(resolution);

            // Invoke
            teleMethod.invoke();
        } catch (Throwable e) {
            logger.error("RPC dispatching exception: {}", ExceptionUtils.getRootCauseMessage(e));
            exchange.writeException(e);
        }
    }

    protected TeleMethod getTeleMethod(RpcExchange.RequestResolution requestResolution) {
        RpcLigature.RpcApi rpcApi = rpcApiMap.get(requestResolution.getApiName());
        if (rpcApi == null) {
            throw new RpcException("RPC API not found: " + requestResolution.getApiName());
        }

        TeleMethod teleMethod = rpcApi.getTeleMethods().get(requestResolution.getMethodName());
        if (teleMethod == null) {
            throw new RpcException("RPC tele method not found: " + requestResolution.getApiName());
        }

        return teleMethod;
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
