package colesico.framework.restlet;

import colesico.framework.telehttp.TeleHttpException;

public final class RestletException extends TeleHttpException {

    public RestletException(String message, Throwable cause, Integer statusCode, Object details) {
        super(message, cause, statusCode, details);
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

}
