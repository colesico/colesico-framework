package colesico.framework.security.authorization;

import colesico.framework.security.Identity;

public record DefaultAuthorizationRequest<R>(Identity<?> identity, R resource)
        implements AuthorizationRequest<R> {
}
