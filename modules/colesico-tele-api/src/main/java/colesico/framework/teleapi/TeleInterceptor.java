package colesico.framework.teleapi;

import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import jakarta.inject.Provider;

public class TeleInterceptor<R extends ReadOptions, W extends WriteOptions> {

    public static final String DATA_PORT_PROV_FIELD = "dataPortProvider";
    public static final String TELE_INTERCEPTOR_SUFFIX = "TeleInterceptor";

    protected final Provider<DataPort<R, W>> dataPortProvider;

    @SuppressWarnings("unchecked")
    public TeleInterceptor(Provider<DataPort> dataPortProvider) {
        this.dataPortProvider = (Provider)dataPortProvider;
    }
}
