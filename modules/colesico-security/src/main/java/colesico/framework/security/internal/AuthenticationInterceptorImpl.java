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

    private final Supplier<Authentication<?,?>> authenticationFactory;
    private final SecurityManager securityManager;
    private final AuthContext authContext;

    public AuthenticationInterceptorImpl(Supplier<Authentication> authenticationFactory,
                                         SecurityManager securityManager,
                                         AuthContext authContext) {
        this.authenticationFactory = (Supplier) authenticationFactory;
        this.securityManager = securityManager;
        this.authContext = authContext;
    }

    @Override
    public Object intercept(InvocationContext context, Options options) {

        Collection<Authentication<?,?>> authentications = new ArrayList<>();

        for (var authenticationClass : options.authentications()) {
            authentications.add(authenticationFactory.get(authenticationClass));
        }

        switch (options.strategy()) {
            case DEFERRED:
                authContext.setAuthentications(authentications);
                return context.proceed();

            case IF_NECESSARY:
                if (securityManager.isAuthenticated()) {
                    return context.proceed();
                }
                // fall-through to STRICT if not authenticated

            case STRICT:
                var result = securityManager.authenticate(authentications);
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
