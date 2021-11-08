package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletTWContext;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class RestletExceptionWriter extends RestletTeleWriter<RestletException> {

    private final ObjectWriter writer;

    public RestletExceptionWriter(Provider<HttpContext> httpContextProv, ObjectWriter writer) {
        super(httpContextProv);
        this.writer = writer;
    }

    @Override
    public void write(RestletException value, RestletTWContext context) {
        if (context.getStatusCode() == null) {
            context.setStatusCode(value.getHttpStatus());
        }
        writer.write(value.getError(), context);
    }
}
