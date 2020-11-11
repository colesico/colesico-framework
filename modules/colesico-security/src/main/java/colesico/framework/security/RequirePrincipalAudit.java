package colesico.framework.security;

import colesico.framework.service.InvocationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Checks that the current principal exists
 */
@Singleton
public final class RequirePrincipalAudit implements AuditInterceptor {

    private final SecurityKit securityKit;

    @Inject
    public RequirePrincipalAudit(SecurityKit securityKit) {
        this.securityKit = securityKit;
    }

    @Override
    public Object audit(InvocationContext context) {
        securityKit.requirePrincipal();
        return context.proceed();
    }
}
