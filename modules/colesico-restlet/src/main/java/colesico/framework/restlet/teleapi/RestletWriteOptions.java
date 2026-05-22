package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpWriteOptions;

import java.lang.reflect.Type;

public final class RestletWriteOptions extends HttpWriteOptions {

    public static final String OF_METHOD = "of";

    /**
     * Custom writer class or null.
     * If null - default writer will be used
     */
    private final Class<? extends RestletTeleWriter> writerClass;

    /**
     * Http code to  return to  client
     */
    private Integer statusCode;

    private RestletWriteOptions(Type valueType, Class<? extends RestletTeleWriter> writerClass, Integer statusCode) {
        super(valueType);
        this.writerClass = writerClass;
        this.statusCode = statusCode;
    }

    public static RestletWriteOptions of(Type valueType) {
        return new RestletWriteOptions(valueType, null, null);
    }

    public static RestletWriteOptions of(Type valueType, Class<? extends RestletTeleWriter> writerClass) {
        return new RestletWriteOptions(valueType, writerClass, null);
    }

    public static RestletWriteOptions of(Type valueType, Class<? extends RestletTeleWriter> writerClass, Integer statusCode) {
        return new RestletWriteOptions(valueType, writerClass, statusCode);
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
