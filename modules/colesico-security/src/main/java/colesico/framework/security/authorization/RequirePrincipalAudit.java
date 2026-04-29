package colesico.framework.security.authorization;

import colesico.framework.security.SecurityManager;
import colesico.framework.service.InvocationContext;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Checks that the current principal exists
 */
@Singleton
public final class RequirePrincipalAudit implements AuditInterceptor {

    private final colesico.framework.security.SecurityManager securityManager;

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
