package colesico.framework.service.interception;

import colesico.framework.assist.ExceptionUtils;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import jakarta.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeleInterceptor<R extends ReadOptions, W extends WriteOptions> {

    public static final String LOGGER_FIELD = "logger";
    public static final String DATA_PORT_PROV_FIELD = "dataPortProvider";

    public static final String PROCEED_METHOD = "proceed";

    public static final String TELE_INTERCEPTOR_SUFFIX = "Interceptor";

    protected static final Logger logger = LoggerFactory.getLogger(TeleInterceptor.class);

    protected final Provider<DataPort<R, W>> dataPortProvider;

    @SuppressWarnings("unchecked")
    public TeleInterceptor(Provider<DataPort> dataPortProvider) {
        this.dataPortProvider = (Provider) dataPortProvider;
    }

    /**
     * Used to call from an interceptor implementation.
     */
    protected <T> T proceed(InvocationContext context, DataPort<R, W> dataPort) {
        try {
            return (T) context.proceed();
        } catch (Exception ex) {
            logger.error("Invocation context proceed error", ex);
            dataPort.write(ex, Exception.class);
        }
        return null;
    }
}
