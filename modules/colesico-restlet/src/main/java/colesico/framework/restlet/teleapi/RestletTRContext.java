package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.OriginFacade;

import java.lang.reflect.Type;

public final class RestletTRContext extends HttpTRContext {

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends RestletTeleReader> readerClass;

    private Type valueType;

    public RestletTRContext(String name, OriginFacade originFacade, Class<? extends RestletTeleReader> readerClass) {
        super(name, originFacade);
        this.readerClass = readerClass;
    }

    public RestletTRContext(String name, OriginFacade originFacade) {
        super(name, originFacade);
        this.readerClass = null;
    }

    public RestletTRContext() {
        super(null, null);
        this.readerClass = null;
    }

    public Class<? extends RestletTeleReader> getReaderClass() {
        return readerClass;
    }

    public Type getValueType() {
        return valueType;
    }

    public void setValueType(Type valueType) {
        this.valueType = valueType;
    }
}
