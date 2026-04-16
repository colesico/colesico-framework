package colesico.framework.security.authorization.authorizers;

import colesico.framework.security.authorization.AuthorizationContext;
import colesico.framework.security.authorization.AuthorizationResult;
import colesico.framework.security.authorization.Authorizer;

import java.util.Collection;

/**
 * Ideal for plugin systems.
 * If at least one {@link Authorizer} has granted access  - access is granted.
 */
public final class AffirmativeAuthorizer implements Authorizer {

    private final Collection<Authorizer<?,?,?>> authorizers;

    public AffirmativeAuthorizer(Collection<Authorizer<?,?,?>> authorizers) {
        this.authorizers = authorizers;
    }

    @Override
    public AuthorizationResult<?> authorize(AuthorizationContext context) {
        for (var authorizer : authorizers) {
            var result = authorizer.authorize(context);
            if (result.isGranted()) {
                return result;
            }
        }
        return AuthorizationResult.denied(null);
    }
}
