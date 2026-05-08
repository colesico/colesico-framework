package colesico.framework.security.assist.authorizer;

import colesico.framework.security.authorization.AuthorizationContext;
import colesico.framework.security.authorization.AuthorizationResult;
import colesico.framework.security.authorization.Authorizer;

public class IdentityRequiredAuthorizer implements Authorizer<Object, Object> {

    @Override
    public AuthorizationResult<Object> authorize(AuthorizationContext<Object> context) {
        if (context.identity() == null) {
            return AuthorizationResult.denied();
        }
        return AuthorizationResult.granted();
    }
}
