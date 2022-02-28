package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTRContext;

import java.lang.reflect.Type;

public final class RestletTRContext extends HttpTRContext {

    public static final String OF_METHOD = "of";

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends RestletTeleReader> readerClass;

    private RestletTRContext(Type valueType, String paramName, String originName, Class<? extends RestletTeleReader> readerClass) {
        super(valueType, paramName, originName);
        this.readerClass = readerClass;
    }

    public Class<? extends RestletTeleReader> getReaderClass() {
        return readerClass;
    }

    public static RestletTRContext of(Type valueType, String paramName, String originName, Class<? extends RestletTeleReader> readerClass) {
        return new RestletTRContext(valueType, paramName, originName, readerClass);
    }

    public static RestletTRContext of(Type valueType, String paramName, String originName) {
        return new RestletTRContext(valueType, paramName, originName, null);
    }

    public static RestletTRContext of(Type valueType, String paramName) {
        return new RestletTRContext(valueType, paramName, RestletOrigin.AUTO, null);
    }

    public static RestletTRContext of(Type valueType) {
        return new RestletTRContext(valueType, null, RestletOrigin.AUTO, null);
    }

}
