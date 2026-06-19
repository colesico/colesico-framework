package colesico.framework.restlet;

public final class RestletException extends RuntimeException {

    /**
     * Returned data
     */
    private final Object details;

    /**
     * Http response status code
     */
    private final Integer statusCode;

    public RestletException(String message, Throwable cause, Object details, Integer statusCode) {
        super(message, cause);
        this.details = details;
        this.statusCode = statusCode;
    }

    public Object details() {
        return details;
    }

    public Integer statusCode() {
        return statusCode;
    }

    public static RestletException of(Object payload, Integer httpStatus) {
        return new RestletException(String.valueOf(payload), null, payload, httpStatus);
    }

    public static RestletException of(String message, Object payload, Integer httpStatus) {
        return new RestletException(message, null, payload, httpStatus);
    }

    public static RestletException of(Throwable cause, Object payload, Integer httpStatus) {
        return new RestletException(String.valueOf(payload), cause, payload, httpStatus);
    }
}
