package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletTWContext;
import colesico.framework.security.PrincipalRequiredException;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class PrincipalRequiredExceptionWriter extends AbstractExceptionWriter<PrincipalRequiredException> {

    public PrincipalRequiredExceptionWriter(Provider<HttpContext> httpContextProv, ObjectWriter writer) {
        super(httpContextProv, writer);
    }

    @Override
    protected Object getDetails(PrincipalRequiredException value, RestletTWContext context) {
        return "User is not authenticated";
    }

    @Override
    protected int getHttpStatus(PrincipalRequiredException value, RestletTWContext context) {
        return 401;
    }
}
