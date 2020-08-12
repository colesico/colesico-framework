package colesico.framework.rpc.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.UnknownRpcClassError;
import colesico.framework.rpc.UnknownRpcMethodError;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Iterator;
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
     * Class names to methods map
     */
    protected final Map<String, Map<String, TeleHandler>> targetsMap = new HashMap<>();

    public RpcController(@Classed(Rpc.class) Polysupplier<TeleFacade> teleFacadesSupp,
                         Ioc ioc,
                         ThreadScope threadScope) {

        this.ioc = ioc;
        this.threadScope = threadScope;
        loadTargets(teleFacadesSupp);

    }

    public void dispatch(RpcExchange exchange) {
        RpcRequest request = null;
        final RpcResponse response = new RpcResponse();
        try {
            request = exchange.readRequest();
            TeleHandler teleMethod = findTeleMethod(request);
            instantiateDataPort(request, response);
            teleMethod.execute();
        } catch (RpcException re) {
            handleError(re.getError(), request, response);
        } catch (Exception e) {
            handleException(e, request, response);
        } finally {
            exchange.writeResponse(response);
        }
    }

    protected void handleException(Exception e, RpcRequest request, RpcResponse response) {
        String className = getTargetClass(request);
        String methodName = getTargetMethod(request);
        logger.error("RPC method '{}->{}' invocation error: {}" + className, methodName, ExceptionUtils.getRootCauseMessage(e));
        RpcError error = new RpcError();
        error.setMessage(ExceptionUtils.getRootCauseMessage(e));
        response.setResult(error);
    }

    protected void handleError(RpcError error, RpcRequest request, RpcResponse response) {
        String className = getTargetClass(request);
        String methodName = getTargetMethod(request);
        logger.error("RPC method '{}->{}' invocation error: {}", className, methodName, error.getMessage());
        response.setResult(error);
    }

    protected void loadTargets(Polysupplier<TeleFacade> teleFacadeSupp) {
        logger.debug("Lookup RPC tele-facades... ");

        Iterator<TeleFacade> it = teleFacadeSupp.iterator(null);
        while (it.hasNext()) {
            TeleFacade teleFacade = it.next();
            logger.debug("Found RPC tele-facade: {}", teleFacade.getClass().getName());
            RpcLigature ligature = (RpcLigature) teleFacade.getLigature();

            Map<String, TeleHandler> classMethods = targetsMap.get(ligature.getTargetClass());
            if (classMethods == null) {
                classMethods = new HashMap<>();
                targetsMap.put(ligature.getTargetClass(), classMethods);
            }

            for (Map.Entry<String, TeleHandler> methodInfo : ligature.getTargetMethods().entrySet()) {
                classMethods.put(methodInfo.getKey(), methodInfo.getValue());
                if (logger.isDebugEnabled()) {
                    logger.debug("RPC method "
                            + ligature.getTargetClass() + "->" + methodInfo.getKey()
                            + " has been registered)");

                }
            }
        }
    }

    protected void instantiateDataPort(RpcRequest request, RpcResponse response) {
        RpcDataPort dataPort = new RpcDataPortImpl(request, response, ioc);
        threadScope.put(DataPort.SCOPE_KEY, dataPort);
    }

    protected TeleHandler findTeleMethod(RpcRequest request) {
        Map<String, TeleHandler> classMethods = targetsMap.get(request.getTargetClass());
        if (classMethods == null) {
            throw new RpcException(new UnknownRpcClassError("RPC tele-facade not found for class " + request.getTargetClass(), request.getTargetClass()));
        }

        TeleHandler teleMethod = classMethods.get(request.getTargetMethod());
        if (teleMethod == null) {
            throw new RpcException(new UnknownRpcMethodError("RPC tele-method '" + request.getTargetMethod() + "' not found for class " + request.getTargetClass(),
                    request.getTargetClass(), request.getTargetMethod()));
        }

        return teleMethod;
    }

    protected String getTargetClass(RpcRequest request) {
        return request == null ? "?" : request.getTargetClass();
    }

    protected String getTargetMethod(RpcRequest request) {
        return request == null ? "?" : request.getTargetMethod();
    }

}
