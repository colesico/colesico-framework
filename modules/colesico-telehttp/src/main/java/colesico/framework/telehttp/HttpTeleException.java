package colesico.framework.telehttp;

import colesico.framework.teleapi.TeleException;

public class HttpTeleException extends TeleException {

    public HttpTeleException() {
    }

    public HttpTeleException(String message) {
        super(message);
    }

    public HttpTeleException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpTeleException(Throwable cause) {
        super(cause);
    }

    public HttpTeleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
