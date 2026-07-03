package colesico.framework.restlet;

import colesico.framework.telehttp.HttpTeleException;

public final class RestletException extends HttpTeleException {

    public RestletException(String message, Throwable cause, Integer statusCode, Object details) {
        super(message, cause, statusCode, details);
    }

    public static RestletException of(Integer statusCode, Object details) {
        return new RestletException(String.valueOf(details), null, statusCode, details);
    }

    public static RestletException of(String message, Integer statusCode) {
        return new RestletException(message, null, statusCode, null);
    }

    public static RestletException of(String message, Integer statusCode, Object details) {
        return new RestletException(message, null, statusCode, details);
    }

    public static RestletException of(Throwable cause, Integer statusCode) {
        return new RestletException(null, cause, statusCode, null);
    }

    public static RestletException of(Throwable cause, Integer statusCode, Object details) {
        return new RestletException(String.valueOf(details), cause, statusCode, details);
    }

    public static RestletException of(String message, Throwable cause, Integer statusCode, Object details) {
        return new RestletException(message, cause, statusCode, details);
    }

}
