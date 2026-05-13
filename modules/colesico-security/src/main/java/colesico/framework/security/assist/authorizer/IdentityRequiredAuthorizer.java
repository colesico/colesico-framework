package colesico.framework.security.assist.authorizer;

import colesico.framework.security.authorization.AuthorizationRequest;
import colesico.framework.security.authorization.AuthorizationResult;
import colesico.framework.security.authorization.Authorizer;

public class IdentityRequiredAuthorizer implements Authorizer<Object, Object> {

    @Override
    public AuthorizationResult<Object> authorize(AuthorizationRequest<Object> request) {
        if (request.identity() == null) {
            return AuthorizationResult.denied();
        }
        return AuthorizationResult.granted();
    }
}
