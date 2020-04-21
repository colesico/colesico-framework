package colesico.framework.rpc.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.rpc.Error;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.UnknownRpcClassError;
import colesico.framework.rpc.UnknownRpcMethodError;
import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcRequestDispatcher;
import colesico.framework.rpc.teleapi.RpcLigature;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleMethod;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Singleton
public class RpcRequestDispatcherImpl implements RpcRequestDispatcher {

    protected final Logger log = LoggerFactory.getLogger(RpcRequestDispatcher.class);
    protected final RpcDataPort dataPort;

    /**
     * Class name to methods map
     */
    protected final Map<String, Map<String, TeleMethod>> classToMethods = new HashMap<>();

    public RpcRequestDispatcherImpl(@Classed(RpcRequestDispatcher.class) Polysupplier<TeleFacade> teleFacadesSupp, RpcDataPort dataPort) {
        this.dataPort = dataPort;
        loadMethodsMapping(teleFacadesSupp);
    }

    protected void loadMethodsMapping(Polysupplier<TeleFacade> teleFacadeSupp) {
        log.debug("Lookup RPC tele-facades... ");

        Iterator<TeleFacade> it = teleFacadeSupp.iterator(null);
        while (it.hasNext()) {
            TeleFacade teleFacade = it.next();
            log.debug("Found RPC tele-facade: " + teleFacade.getClass().getName());
            RpcLigature ligature = (RpcLigature) teleFacade.getLigature();

            Map<String, TeleMethod> classMethods = classToMethods.get(ligature.getClassName());
            if (classMethods == null) {
                classMethods = new HashMap<>();
                classToMethods.put(ligature.getClassName(), classMethods);
            }

            for (Map.Entry<String, TeleMethod> methodInfo : ligature.getMethods().entrySet()) {
                classMethods.put(methodInfo.getKey(), methodInfo.getValue());
                if (log.isDebugEnabled()) {
                    log.debug("RPC method "
                            + ligature.getClassName() + "->" + methodInfo.getKey()
                            + " has been registered)");

                }
            }
        }
    }

    @Override
    public void dispatch(String className, String methodName) {
        try {
            Map<String, TeleMethod> classMethods = classToMethods.get(className);
            if (classMethods == null) {
                throw new RpcException(new UnknownRpcClassError("RPC tele-facade not found for class " + className, className));
            }

            TeleMethod teleMethod = classMethods.get(methodName);
            if (teleMethod == null) {
                throw new RpcException(new UnknownRpcMethodError("RPC tele-method '" + methodName + "' not found for class " + className,
                        className, methodName));
            }

            teleMethod.invoke();

        } catch (RpcException re) {
            handleError(re.getError(), className, methodName);
        } catch (Exception e) {
            handleException(e, className, methodName);
        }
    }

    protected void handleException(Exception e, String className, String methodName) {
        log.error("RPC method '" + className + "->" + methodName + "' invocation error: " + ExceptionUtils.getRootCauseMessage(e));
        Error error = new Error();
        error.setMessage(ExceptionUtils.getRootCauseMessage(e));
        dataPort.writeError(error);
    }

    protected void handleError(Error error, String className, String methodName) {
        log.error("RPC method '" + className + "->" + methodName + "' invocation error: " + error.getMessage());
        dataPort.writeError(error);
    }

}
