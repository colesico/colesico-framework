package colesico.framework.telehttp;

/**
 * General tele http exception
 * with http status code support
 */
public class TeleHttpException extends RuntimeException {

    /**
     * Http value status code
     */
    protected final Integer statusCode;

    /**
     * Exception details object
     */
    protected final Object details;

    public TeleHttpException(String message, Throwable cause, Integer statusCode, Object details) {
        super(message, cause);
        this.statusCode = statusCode;
        this.details = details;
    }

    public static TeleHttpException of(Object details) {
        return new TeleHttpException(String.valueOf(details), null, null, details);
    }

    public static TeleHttpException of(Throwable cause) {
        return new TeleHttpException(null, cause, null, null);
    }

    public static TeleHttpException of(Integer statusCode, Object details) {
        return new TeleHttpException(String.valueOf(details), null, statusCode, details);
    }

    public static TeleHttpException of(String message, Integer statusCode, Object details) {
        return new TeleHttpException(message, null, statusCode, details);
    }

    public static TeleHttpException of(Throwable cause, Integer statusCode, Object details) {
        return new TeleHttpException(String.valueOf(details), cause, statusCode, details);
    }

    public Integer statusCode() {
        return statusCode;
    }

    public Object details() {
        return details;
    }
}
