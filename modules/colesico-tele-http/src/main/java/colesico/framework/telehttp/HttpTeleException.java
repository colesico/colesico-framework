package colesico.framework.telehttp;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTeleException extends RuntimeException {

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

    public HttpTeleException(String message, Throwable cause, Integer statusCode, Object details) {
        super(message, cause);
        this.statusCode = statusCode;
        this.details = details;
    }

    public static HttpTeleException of(Object details) {
        return new HttpTeleException(String.valueOf(details), null, DEFAULT_STATUS_CODE, details);
    }

    public static HttpTeleException of(Throwable cause) {
        return new HttpTeleException(null, cause, DEFAULT_STATUS_CODE, null);
    }

    public static HttpTeleException of(Integer statusCode, Object details) {
        return new HttpTeleException(String.valueOf(details), null, statusCode, details);
    }

    public static HttpTeleException of(String message, Integer statusCode, Object details) {
        return new HttpTeleException(message, null, statusCode, details);
    }

    public static HttpTeleException of(Throwable cause, Integer statusCode, Object details) {
        return new HttpTeleException(String.valueOf(details), cause, statusCode, details);
    }

    public Integer statusCode() {
        return statusCode;
    }

    public Object details() {
        return details;
    }
}
