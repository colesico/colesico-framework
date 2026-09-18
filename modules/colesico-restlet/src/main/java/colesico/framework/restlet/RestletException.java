package colesico.framework.restlet;

import colesico.framework.telehttp.HttpTeleException;

public final class RestletException extends HttpTeleException {

    public RestletException() {
    }

    public RestletException(String message) {
        super(message);
    }

    public RestletException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestletException(Throwable cause) {
        super(cause);
    }

    public RestletException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
