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
    private Integer statusCode;

    private RestletTWContext(Type valueType, Class<? extends RestletTeleWriter> writerClass, Integer statusCode) {
        super(valueType);
        this.writerClass = writerClass;
        this.statusCode = statusCode;
    }

    public static RestletTWContext of(Type valueType) {
        return new RestletTWContext(valueType, null, null);
    }

    public static RestletTWContext of(Type valueType, Class<? extends RestletTeleWriter> writerClass) {
        return new RestletTWContext(valueType, writerClass, null);
    }

    public static RestletTWContext of(Type valueType, Class<? extends RestletTeleWriter> writerClass, Integer statusCode) {
        return new RestletTWContext(valueType, writerClass, statusCode);
    }

    public Class<? extends RestletTeleWriter> getWriterClass() {
        return writerClass;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
