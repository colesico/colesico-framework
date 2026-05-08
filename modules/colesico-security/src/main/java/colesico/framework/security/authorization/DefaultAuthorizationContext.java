package colesico.framework.security.authorization;

import colesico.framework.security.Identity;

public record DefaultAuthorizationContext<R>(Identity<?> identity, R resource)
        implements AuthorizationContext<R> {
}
