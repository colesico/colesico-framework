package colesico.framework.example.restlet.customexception;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletTWContext;
import colesico.framework.restlet.teleapi.writer.AbstractExceptionWriter;
import colesico.framework.restlet.teleapi.writer.ObjectWriter;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Writer to send custom exception to client
 * @see colesico.framework.restlet.RestletError
 */
@Singleton
public class CustomExceptionWriter extends AbstractExceptionWriter<CustomException> {

    public CustomExceptionWriter(Provider<HttpContext> httpContextProv, ObjectWriter writer) {
        super(httpContextProv, writer);
    }

    @Override
    protected Object getDetails(CustomException value, RestletTWContext context) {
        return value.getPayload();
    }

    @Override
    protected int getHttpCode(CustomException value, RestletTWContext context) {
        return 505;
    }
}
