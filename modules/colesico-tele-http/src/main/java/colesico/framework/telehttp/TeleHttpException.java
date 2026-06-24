package colesico.framework.telehttp;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TeleHttpException extends RuntimeException {

    public static final Integer DEFAULT_STATUS_CODE = 500;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Http response status code
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
        return new TeleHttpException(String.valueOf(details), null, DEFAULT_STATUS_CODE, details);
    }

    public static TeleHttpException of(Throwable cause) {
        return new TeleHttpException(null, cause, DEFAULT_STATUS_CODE, null);
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
