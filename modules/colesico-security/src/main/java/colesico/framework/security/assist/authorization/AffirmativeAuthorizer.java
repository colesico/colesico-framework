package colesico.framework.security.assist.authorization;

import colesico.framework.security.authorization.AuthorizationRequest;
import colesico.framework.security.authorization.AuthorizationResult;
import colesico.framework.security.authorization.Authorizer;

import java.util.Collection;

/**
 * Ideal for plugin systems.
 * If at least one {@link Authorizer} has granted access  - access is granted.
 */
public final class AffirmativeAuthorizer<R,D> implements Authorizer<R,D> {

    private final Collection<Authorizer<R,D>> authorizers;

    public AffirmativeAuthorizer(Collection<Authorizer<R,D>> authorizers) {
        this.authorizers = authorizers;
    }

    @Override
    public AuthorizationResult<D> authorize(AuthorizationRequest<R> request) {
        for (var authorizer : authorizers) {
            var result = authorizer.authorize(request);
            if (result.isGranted()) {
                return result;
            }
        }
        return AuthorizationResult.denied(null);
    }
}
