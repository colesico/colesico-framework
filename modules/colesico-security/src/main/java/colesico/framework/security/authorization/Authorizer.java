package colesico.framework.security.authorization;

import colesico.framework.security.Principal;

/**
 * Responsible for checking the ability to perform a business action.
 * Authorizers can be organized to nested structure
 */
public interface Authorizer<P extends Principal<?>, R, D> {

    /**
     * Perform authorization
     */
    AuthorizationResult<D> authorize(AuthorizationContext<P, R> context);
}
