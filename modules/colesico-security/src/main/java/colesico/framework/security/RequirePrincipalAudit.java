package colesico.framework.security;

import colesico.framework.service.InvocationContext;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Checks that the current principal exists
 */
@Singleton
public final class RequirePrincipalAudit implements AuditInterceptor {

    private final SecurityManager securityManager;

    @Inject
    public RequirePrincipalAudit(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public Object audit(InvocationContext context) {
        securityManager.requirePrincipal();
        return context.proceed();
    }
}
