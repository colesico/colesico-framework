package colesico.framework.security.authorization;

import colesico.framework.security.SecurityManager;
import colesico.framework.service.InvocationContext;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Checks that the current identity exists
 */
@Singleton
public final class RequireIdentityAudit implements AuditInterceptor {

    private final colesico.framework.security.SecurityManager securityManager;

    @Inject
    public RequireIdentityAudit(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public Object audit(InvocationContext context) {
        securityManager.requireIdentity();
        return context.proceed();
    }
}
