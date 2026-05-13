package colesico.framework.security.authorization;

import colesico.framework.security.Identity;

/**
 * Request for authorization given identity to resource
 */
public interface AuthorizationRequest<R> {

    /**
     * Identity that requests authorization
     */
    Identity<?> identity();

    /**
     * Resource for which authorization is requested.
     */
    R resource();

    /**
     * Default implementation
     */
    record Default<R>(Identity<?> identity, R resource)
            implements AuthorizationRequest<R> {
    }
}
