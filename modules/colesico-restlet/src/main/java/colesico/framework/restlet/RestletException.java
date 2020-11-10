package colesico.framework.restlet;

public final class RestletException extends RuntimeException {
    private final RestletError error;
    private final int httpCode;

    public RestletException(RestletError error) {
        this.error = error;
        httpCode = 500;
    }

    public RestletException(RestletError error, int httpCode) {
        this.error = error;
        this.httpCode = httpCode;
    }

    public RestletError getError() {
        return error;
    }

    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public String getMessage() {
        return error == null ? super.getMessage() : error.getMessage();
    }
}
