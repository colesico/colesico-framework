package colesico.framework.restlet.teleapi.writer;

import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

/**
 * Exception writer helper
 */
abstract public class AbstractExceptionWriter<T extends Throwable> implements RestletTeleWriter<T> {

    private final ObjectWriter writer;

    public AbstractExceptionWriter(ObjectWriter writer) {
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
        options.setStatusCode(getHttpStatus(value, options));
        writer.write(error, Object.class, options);
    }
}
