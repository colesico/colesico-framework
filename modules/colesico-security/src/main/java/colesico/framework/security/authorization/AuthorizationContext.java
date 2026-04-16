package colesico.framework.security.authorization;

import colesico.framework.security.Principal;

public interface AuthorizationContext<P extends Principal<?>, R> {

    /**
     * Principal that requests authorization
     */
    P principal();

    /**
     * Resource for which authorization is requested.
     */
    R resource();

}
