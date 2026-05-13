package colesico.framework.security.authorization;

/**
 * Responsible for checking the ability to perform a business action.
 * Authorizers can be organized to nested structure
 */
public interface Authorizer<R, D> {

    /**
     * Perform specific authorization
     */
    AuthorizationResult<D> authorize(AuthorizationRequest<R> request);
}
