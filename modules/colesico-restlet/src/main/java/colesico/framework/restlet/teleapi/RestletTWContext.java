package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTWContext;

import java.lang.reflect.Type;

public final class RestletTWContext extends HttpTWContext {

    public static final String OF_METHOD = "of";

    /**
     * Custom writer class or null.
     * If null - default writer will be used
     */
    private final Class<? extends RestletTeleWriter> writerClass;

    /**
     * Http code to return to client
     */
    private Integer httpCode;

    private RestletTWContext(Type valueType, Class<? extends RestletTeleWriter> writerClass, Integer httpCode) {
        super(valueType);
        this.writerClass = writerClass;
        this.httpCode = httpCode;
    }

    public static RestletTWContext of(Type valueType) {
        return new RestletTWContext(valueType, null, null);
    }

    public static RestletTWContext of(Type valueType, Class<? extends RestletTeleWriter> writerClass) {
        return new RestletTWContext(valueType, writerClass, null);
    }

    public static RestletTWContext of(Type valueType, Class<? extends RestletTeleWriter> writerClass, Integer httpCode) {
        return new RestletTWContext(valueType, writerClass, httpCode);
    }

    public Class<? extends RestletTeleWriter> getWriterClass() {
        return writerClass;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }
}
