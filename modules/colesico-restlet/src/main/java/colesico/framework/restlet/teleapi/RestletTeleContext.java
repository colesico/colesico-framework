package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTeleContext;

import java.lang.reflect.Type;

public final class RestletTeleContext extends HttpTeleContext {

    public static final String OF_METHOD = "of";

    /**
     * Custom reader class or null.
     * If null - default reader will be used to  read the parameter
     */
    private final Class<? extends RestletTeleReader> readerClass;

    private RestletTeleContext(Type valueType, String paramName, String originName, Class<? extends RestletTeleReader> readerClass) {
        super(valueType, paramName, originName);
        this.readerClass = readerClass;
    }

    public Class<? extends RestletTeleReader> getReaderClass() {
        return readerClass;
    }

    public static RestletTeleContext of(Type valueType, String paramName, String originName, Class<? extends RestletTeleReader> readerClass) {
        return new RestletTeleContext(valueType, paramName, originName, readerClass);
    }

    public static RestletTeleContext of(Type valueType, String paramName, String originName) {
        return new RestletTeleContext(valueType, paramName, originName, null);
    }

    public static RestletTeleContext of(Type valueType, String paramName) {
        return new RestletTeleContext(valueType, paramName, RestletOrigin.AUTO, null);
    }

    public static RestletTeleContext of(Type valueType) {
        return new RestletTeleContext(valueType, null, RestletOrigin.AUTO, null);
    }

}
