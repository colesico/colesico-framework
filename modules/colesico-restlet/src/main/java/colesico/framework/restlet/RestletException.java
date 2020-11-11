package colesico.framework.restlet;

public final class RestletException extends RuntimeException {
    private final RestletError error;

    /**
     * Http response status code
     */
    private final int httpStatus;

    public RestletException(RestletError error) {
        this.error = error;
        httpStatus = 500;
    }

    public RestletException(RestletError error, int httpStatus) {
        this.error = error;
        this.httpStatus = httpStatus;
    }

    public RestletError getError() {
        return error;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return error == null ? super.getMessage() : error.getMessage();
    }
}
