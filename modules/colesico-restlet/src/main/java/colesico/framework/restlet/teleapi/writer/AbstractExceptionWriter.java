package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.teleapi.RestletTWContext;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import javax.inject.Provider;

/**
 * Exception writer helper
 */
abstract public class AbstractExceptionWriter<T extends Throwable> extends RestletTeleWriter<T> {

    private final ObjectWriter writer;

    public AbstractExceptionWriter(Provider<HttpContext> httpContextProv, ObjectWriter writer) {
        super(httpContextProv);
        this.writer = writer;
    }

    abstract protected Object getDetails(T value, RestletTWContext context);

    abstract protected int getHttpCode(T value, RestletTWContext context);

    protected String getErrorCode(T value, RestletTWContext context) {
        return value.getClass().getCanonicalName();
    }

    protected String getMessage(T value, RestletTWContext context) {
        return value.getMessage();
    }

    @Override
    public void write(T value, RestletTWContext context) {
        RestletError error = new RestletError();
        error.setErrorCode(getErrorCode(value, context));
        error.setMessage(getMessage(value, context));
        error.setDetails(getDetails(value, context));
        context.setHttpCode(getHttpCode(value, context));
        writer.write(error, context);
    }
}
