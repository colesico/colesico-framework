package colesico.framework.security.authorization;

import colesico.framework.security.Identity;

public interface AuthorizationContext<R> {

    /**
     * Identity that requests authorization
     */
    Identity<?> identity();

    /**
     * Resource for which authorization is requested.
     */
    R resource();

}
