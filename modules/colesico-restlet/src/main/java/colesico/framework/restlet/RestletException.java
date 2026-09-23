package colesico.framework.restlet;

import colesico.framework.telehttp.HttpTeleException;

public final class RestletException extends HttpTeleException {

    public RestletException() {
    }

    public RestletException(ProblemDetails problemDetails, Integer status) {
        super(problemDetails, status);
    }

    public RestletException(String message, Integer status) {
        super(message, status);
    }

    public RestletException(Throwable cause, Integer status) {
        super(cause, status);
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
}
