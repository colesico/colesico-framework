package colesico.framework.rpc.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleMethod;
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
     */
    protected final Map<String, RpcLigature.RpcApi> targetsMap = new HashMap<>();

    public RpcController(@Classed(Rpc.class) Polysupplier<TeleFacade> teleFacadesSupp,
                         Ioc ioc,
                         ThreadScope threadScope) {

        this.ioc = ioc;
        this.threadScope = threadScope;
        loadTargets(teleFacadesSupp);

    }

    public void dispatch(RpcExchange exchange) {
        RpcRequest request = null;
        final RpcResponse response = null;
        try {
            //request = exchange.readRequest();
            TeleMethod teleMethod = findTeleMethod(request);
            instantiateDataPort(request, response);
            teleMethod.invoke();
        } catch (Exception e) {
            handleException(e, request, response);
        } finally {
            exchange.writeResponse(response);
        }
    }

    protected void handleException(Exception e, RpcRequest request, RpcResponse response) {
       // TODO:
    }

    protected void loadTargets(Polysupplier<TeleFacade> teleFacadeSupp) {
        logger.debug("Lookup RPC tele-facades... ");

        Iterator<TeleFacade> it = teleFacadeSupp.iterator(null);

        while (it.hasNext()) {
            TeleFacade teleFacade = it.next();
            logger.debug("Found RPC tele-facade: {}", teleFacade.getClass().getName());
            RpcLigature ligature = (RpcLigature) teleFacade.getLigature();

            List<RpcLigature.RpcApi> rpcApiList = ligature.getAllRpcApi();
            for (RpcLigature.RpcApi rpcApi : rpcApiList) {
                RpcLigature.RpcApi prevApi = targetsMap.put(rpcApi.getName(), rpcApi);
                if (prevApi != null) {
                    throw new RpcException("Duplicate RPC API implementation: " + rpcApi.getName());
                }
            }
        }
    }

    protected void instantiateDataPort(RpcRequest request, RpcResponse response) {
        RpcDataPort dataPort = new RpcDataPortImpl(request, response, ioc);
        threadScope.put(DataPort.SCOPE_KEY, dataPort);
    }

    protected TeleMethod findTeleMethod(RpcRequest request) {
        // TODO:
        return null;
    }


}
