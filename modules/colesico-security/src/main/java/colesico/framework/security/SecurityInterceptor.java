package colesico.framework.security;

import colesico.framework.service.InvocationContext;

public interface SecurityInterceptor extends SecurityKit {
    String INTERCEPT_REQUIRE_PRINCIPAL_METHOD="interceptRequirePrincipal";
    String INTERCEPT_REQUIRE_AUTHORITY_METHOD="interceptRequireAuthority";

    Object interceptRequirePrincipal(InvocationContext context);
    Object interceptRequireAuthority(InvocationContext context);
}
