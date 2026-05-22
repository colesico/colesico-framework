package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import jakarta.inject.Provider;

/**
 * Exception writer helper
 */
abstract public class AbstractExceptionWriter<T extends Throwable> extends RestletTeleWriter<T> {

    private final ObjectWriter writer;

    public AbstractExceptionWriter(Provider<HttpContext> httpContextProv, ObjectWriter writer) {
        super(httpContextProv);
        this.writer = writer;
    }

    abstract protected Object getDetails(T value, RestletWriteOptions context);

    /**
     * HTTP response status code to  sent to  client
     */
    protected int getHttpStatus(T value, RestletWriteOptions context){
        return 500;
    }

    protected String getErrorCode(T value, RestletWriteOptions context) {
        return value.getClass().getCanonicalName();
    }

    protected String getMessage(T value, RestletWriteOptions context) {
        return value.getMessage();
    }

    @Override
    public void write(T value, RestletWriteOptions context) {
        RestletError error = new RestletError();
        error.setErrorCode(getErrorCode(value, context));
        error.setMessage(getMessage(value, context));
        error.setDetails(getDetails(value, context));
        context.setStatusCode(getHttpStatus(value, context));
        writer.write(error, context);
    }
}
