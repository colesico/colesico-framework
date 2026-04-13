package colesico.framework.security.authorization;

/**
 * Responsible for checking the ability to perform a business action.
 */
public interface Authorizer<T> {
    AuthorizationResult<T> authorize(T context);
}
