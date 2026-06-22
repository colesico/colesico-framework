package colesico.framework.restlet;

public final class RestletException extends RuntimeException {

    /**
     * Http response status code
     */
    private final Integer statusCode;

    /**
     * Returned data
     */
    private final Object details;

    public RestletException(String message, Throwable cause, Integer statusCode, Object details) {
        super(message, cause);
        this.statusCode = statusCode != null ? statusCode : 500;
        this.details = details;
    }

    public static RestletException of(Integer statusCode, Object details) {
        return new RestletException(String.valueOf(details), null, statusCode, details);
    }

    public static RestletException of(String message, Integer statusCode, Object details) {
        return new RestletException(message, null, statusCode, details);
    }

    public static RestletException of(Throwable cause, Integer statusCode, Object details) {
        return new RestletException(String.valueOf(details), cause, statusCode, details);
    }

    public Object details() {
        return details;
    }

    public Integer statusCode() {
        return statusCode;
    }
}
