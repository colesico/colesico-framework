package colesico.framework.security.authorization.authorizers;

import colesico.framework.security.Principal;
import colesico.framework.security.authorization.AuthorizationContext;
import colesico.framework.security.authorization.AuthorizationResult;
import colesico.framework.security.authorization.Authorizer;

public class PrincipalRequiredAuthorizer implements Authorizer<Principal<?>, Object, Object> {

    @Override
    public AuthorizationResult<Object> authorize(AuthorizationContext<Principal<?>, Object> context) {
        if (context.principal() == null) {
            return AuthorizationResult.denied();
        }
        return AuthorizationResult.granted();
    }
}
