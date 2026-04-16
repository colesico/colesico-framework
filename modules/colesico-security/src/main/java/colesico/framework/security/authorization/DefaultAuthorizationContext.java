package colesico.framework.security.authorization;

import colesico.framework.security.Principal;

public record DefaultAuthorizationContext<P extends Principal<?>, D>(P principal, D resource)
        implements AuthorizationContext<P, D> {

}
