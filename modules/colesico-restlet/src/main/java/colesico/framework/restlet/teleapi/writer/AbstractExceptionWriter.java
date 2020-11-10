package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletTWContext;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import javax.inject.Provider;
import javax.inject.Singleton;

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

    abstract protected String getErrorCode(T value, RestletTWContext context);

    abstract protected int getHttpCode(T value, RestletTWContext context);

    protected String getMessage(T value, RestletTWContext context) {
        return value.getMessage();
    }

    @Override
    public void write(T value, RestletTWContext context) {
        RestletError error = new RestletError();
        error.setCode(getErrorCode(value, context));
        error.setMessage(getMessage(value, context));
        error.setDetails(getDetails(value, context));
        context.setHttpCode(getHttpCode(value, context));
        writer.write(error, context);
    }
}
