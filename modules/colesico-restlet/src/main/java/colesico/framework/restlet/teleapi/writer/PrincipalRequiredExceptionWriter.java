package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.security.authorization.PrincipalRequiredException;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class PrincipalRequiredExceptionWriter extends AbstractExceptionWriter<PrincipalRequiredException> {

    public PrincipalRequiredExceptionWriter(Provider<HttpContext> httpContextProv, ObjectWriter writer) {
        super(httpContextProv, writer);
    }

    @Override
    protected Object getDetails(PrincipalRequiredException value, RestletWriteOptions context) {
        return "User is not authenticated";
    }

    @Override
    protected int getHttpStatus(PrincipalRequiredException value, RestletWriteOptions context) {
        return 401;
    }
}
