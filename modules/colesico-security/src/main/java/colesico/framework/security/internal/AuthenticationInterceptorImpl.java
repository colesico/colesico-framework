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

        Collection<AuthenticationSource<?, ?>> sources = new ArrayList<>();

        for (var souceClass : options.sources()) {
            sources.add(sourceFactory.get(souceClass));
        }

        if (options.login()) {
            var result = securityManager.login(sources);
            if (result instanceof AuthenticationResult.Success) {
                return context.proceed();
            } else if (result instanceof AuthenticationResult.Continuation<?>) {
                return null;
            } else {
                throw new UnauthenticatedException("Unauthenticated");
            }
        } else {
            sourceContext.setSources(sources);
            return context.proceed();
        }
    }
}
