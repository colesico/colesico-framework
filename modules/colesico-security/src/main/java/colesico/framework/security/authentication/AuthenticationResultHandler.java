package colesico.framework.security.authentication;

import colesico.framework.service.interception.InvocationContext;

@FunctionalInterface
public interface AuthenticationResultHandler {
    Object handleResult(AuthenticationResult result, InvocationContext context);
}
