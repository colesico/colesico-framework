package colesico.framework.security.internal;

import colesico.framework.ioc.production.Supplier;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.*;
import colesico.framework.security.authentication.AuthenticatorOutcome;
import colesico.framework.service.interception.InvocationContext;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;

@Singleton
public class AuthenticationInterceptorImpl implements AuthenticationInterceptor {

    private final Supplier<AuthenticationSource> sourceFactory;
    private final SecurityManager securityManager;
    private final AuthenticationSourceContext sourceContext;

    public AuthenticationInterceptorImpl(Supplier<AuthenticationSource> sourceFactory,
                                         SecurityManager securityManager,
                                         AuthenticationSourceContext sourceContext) {
        this.sourceFactory = sourceFactory;
        this.securityManager = securityManager;
        this.sourceContext = sourceContext;
    }

    @Override
    public Object intercept(InvocationContext context, Options options) {

        Collection<AuthenticationSource<?,?>> sources = new ArrayList<>();

        for (var sourceClass : options.sources()) {
            sources.add(sourceFactory.get(sourceClass));
        }

        switch (options.strategy()) {
            case DEFERRED:
                sourceContext.setSources(sources);
                return context.proceed();

            case IF_NECESSARY:
                if (securityManager.isAuthenticated()) {
                    return context.proceed();
                }
                // fall-through to STRICT if not authenticated

            case STRICT:
                var result = securityManager.authenticate(sources);
                if (result instanceof AuthenticationResult.Success) {
                    return context.proceed();
                } else if (result instanceof AuthenticationResult.Stage) {
                    return null;
                } else if (result instanceof AuthenticationResult.Failure f) {
                    throw new UnauthenticatedException(f.error() != null ? f.error().toString() : "Unauthenticated");
                } else {
                    throw new IllegalArgumentException("Unsupported authentication result: " + result.toString());
                }
            default:
                throw new IllegalArgumentException("Unsupported strategy: " + options.strategy());
        }
    }
}
