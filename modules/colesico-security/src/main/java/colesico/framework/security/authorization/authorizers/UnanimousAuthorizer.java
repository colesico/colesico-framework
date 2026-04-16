package colesico.framework.security.authorization.authorizers;

import colesico.framework.security.authorization.AuthorizationContext;
import colesico.framework.security.authorization.AuthorizationResult;
import colesico.framework.security.authorization.Authorizer;

import java.util.Collection;

/**
 * For critical systems.
 * If even one {@link Authorizer} denies access to a resource - access is denied.
 */
public final class UnanimousAuthorizer implements Authorizer {

    private final Collection<Authorizer> authorizers;

    public UnanimousAuthorizer(Collection<Authorizer> authorizers) {
        this.authorizers = authorizers;
    }

    @Override
    public AuthorizationResult<?> authorize(AuthorizationContext context) {
        for (var authorizer : authorizers) {
            var result = authorizer.authorize(context);
            if (result.isDenied()) {
                return result;
            }
        }
        return AuthorizationResult.granted();
    }
}
