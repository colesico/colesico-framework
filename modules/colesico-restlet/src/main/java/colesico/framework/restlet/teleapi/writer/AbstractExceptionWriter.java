package colesico.framework.restlet.teleapi.writer;

import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.response.RestletResponse;

/**
 * Exception writer helper
 */
abstract public class AbstractExceptionWriter<T extends Throwable> implements RestletTeleWriter<T> {

    private final RestletResponseWriter writer;

    public AbstractExceptionWriter(RestletResponseWriter writer) {
        this.writer = writer;
    }

    abstract protected Object getDetails(T value, RestletWriteOptions options);

    /**
     * HTTP response status code to  sent to  client
     */
    protected int getHttpStatus(T value, RestletWriteOptions options) {
        return 500;
    }

    protected String getErrorCode(T value, RestletWriteOptions options) {
        return value.getClass().getCanonicalName();
    }

    protected String getMessage(T value, RestletWriteOptions options) {
        return value.getMessage();
    }

    @Override
    public void write(T value, Class<T> valueType, RestletWriteOptions options) {
        RestletError error = new RestletError();
        error.setErrorCode(getErrorCode(value, options));
        error.setMessage(getMessage(value, options));
        error.setDetails(getDetails(value, options));

        var response = RestletResponse.of(error, getHttpStatus(value, options));

        writer.write(response, RestletResponse.class, options);
    }
}
