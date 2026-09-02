package colesico.framework.security.internal;

import colesico.framework.ioc.production.Supplier;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.*;
import colesico.framework.service.interception.InvocationContext;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;

@Singleton
public class AuthenticationInterceptorImpl implements AuthenticationInterceptor {

    private final AuthenticatorFactory authenticatorFactory;
    private final SecurityManager securityManager;
    private final AuthenticationContext authContext;

    public AuthenticationInterceptorImpl(AuthenticatorFactory authenticatorFactory,
                                         SecurityManager securityManager,
                                         AuthenticationContext authContext) {
        this.authenticatorFactory = authenticatorFactory;
        this.securityManager = securityManager;
        this.authContext = authContext;
    }

    @Override
    public Object intercept(InvocationContext context, Options options) {

        Collection<Authenticator<?, ?>> authenticators = new ArrayList<>();

        for (var authSpec : options.authenticators()) {
            authenticators.add(authenticatorFactory.get(authSpec.authenticatorClass(), authSpec.classed()));
        }

        switch (options.strategy()) {
            case DEFERRED:
                authContext.setAuthenticators(authenticators);
                return context.proceed();

            case IF_NECESSARY:
                if (securityManager.isAuthenticated()) {
                    return context.proceed();
                }
                // fall-through to STRICT if not authenticated

            case STRICT:
                var result = securityManager.authenticate(authenticators);
                return switch (result) {
                    case AuthenticationResult.Success _ -> context.proceed();
                    case AuthenticationResult.Stage _ -> null;
                    case AuthenticationResult.Failure f ->
                            throw new UnauthenticatedException(f.error() != null ? f.error().toString() : "Unauthenticated");
                };
            default:
                throw new IllegalArgumentException("Unsupported strategy: " + options.strategy());
        }
    }
}
