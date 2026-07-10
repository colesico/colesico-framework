package colesico.framework.teleapi;

import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import jakarta.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeleInterceptor<R extends ReadOptions, W extends WriteOptions> {

    public static final String LOGGER_FIELD = "logger";
    public static final String DATA_PORT_PROV_FIELD = "dataPortProvider";

    public static final String WRITE_EXCEPTION_METHOD = "writeException";

    public static final String TELE_INTERCEPTOR_SUFFIX = "Interceptor";

    protected static final Logger logger = LoggerFactory.getLogger(TeleInterceptor.class);

    protected final Provider<DataPort<R, W>> dataPortProvider;

    @SuppressWarnings("unchecked")
    public TeleInterceptor(Provider<DataPort> dataPortProvider) {
        this.dataPortProvider = (Provider) dataPortProvider;
    }

    protected void writeException(Exception exception, DataPort<R, W> dataPort) {
        try {
            dataPort.write(exception, Exception.class);
        } catch (Exception e) {
            logger.error("Error writing exception to data port", e);
        }
    }
}
