package colesico.framework.security;

import colesico.framework.service.InvocationContext;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Checks that the current principal exists
 */
@Singleton
public final class RequirePrincipalAudit implements AuditInterceptor {

    private final SecurityContext securityContext;

    @Inject
    public RequirePrincipalAudit(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public Object audit(InvocationContext context) {
        securityContext.requirePrincipal();
        return context.proceed();
    }
}
