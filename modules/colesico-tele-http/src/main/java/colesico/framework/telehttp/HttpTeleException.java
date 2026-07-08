package colesico.framework.telehttp;

/**
 * General tele http exception
 * with http status code support
 */
public class HttpTeleException extends RuntimeException {

    /**
     * Http response status code
     */
    protected final Integer statusCode;

    /**
     * Exception details object
     */
    protected final Object details;

    public HttpTeleException(String message, Throwable cause, Integer statusCode, Object details) {
        super(message, cause);
        this.statusCode = statusCode;
        this.details = details;
    }

    public static HttpTeleException of(Integer statusCode, Object details) {
        return new HttpTeleException(String.valueOf(details), null, statusCode, details);
    }

    public static HttpTeleException of(String message, Integer statusCode) {
        return new HttpTeleException(message, null, statusCode, null);
    }

    public static HttpTeleException of(String message, Integer statusCode, Object details) {
        return new HttpTeleException(message, null, statusCode, details);
    }

    public static HttpTeleException of(Throwable cause, Integer statusCode) {
        return new HttpTeleException(null, cause, statusCode, null);
    }

    public static HttpTeleException of(Throwable cause, Integer statusCode, Object details) {
        return new HttpTeleException(String.valueOf(details), cause, statusCode, details);
    }

    public static HttpTeleException of(String message, Throwable cause, Integer statusCode, Object details) {
        return new HttpTeleException(message, cause, statusCode, details);
    }

    public Integer statusCode() {
        return statusCode;
    }

    public Object details() {
        return details;
    }

    @Override
    public String toString() {
        return "HttpTeleException{" +
                "message=" + getMessage() +
                ", statusCode=" + statusCode +
                ", details=" + details +
                '}';
    }
}
