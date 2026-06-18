package colesico.framework.restlet;

public final class RestletException extends RuntimeException {
    /**
     * Returned data
     */
    private final Object payload;

    /**
     * Http response status code
     */
    private final int httpStatus;

    public RestletException(String message, Throwable cause, Object payload, int httpStatus) {
        super(message, cause);
        this.payload = payload;
        this.httpStatus = httpStatus;
    }

    public Object payload() {
        return payload;
    }

    public int httpStatus() {
        return httpStatus;
    }

    public static RestletException of(Object payload, int httpStatus) {
        return new RestletException(String.valueOf(payload), null, payload, httpStatus);
    }

    public static RestletException of(String message, Object payload, int httpStatus) {
        return new RestletException(message, null, payload, httpStatus);
    }

    public static RestletException of(String message, Object payload, int httpStatus) {
        return new RestletException(message, null, payload, httpStatus);
    }
}
