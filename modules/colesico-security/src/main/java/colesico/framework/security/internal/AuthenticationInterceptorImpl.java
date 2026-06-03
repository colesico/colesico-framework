package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationInterceptor;
import colesico.framework.security.authentication.AuthenticationSource;
import colesico.framework.security.authentication.AuthenticationSourceContext;
import colesico.framework.service.interception.InvocationContext;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;

@Singleton
public class AuthenticationInterceptorImpl implements AuthenticationInterceptor<Object, Object> {

    private final Ioc ioc;
    private final SecurityManager securityManager;
    private final AuthenticationSourceContext sourceContext;

    public AuthenticationInterceptorImpl(Ioc ioc, SecurityManager securityManager, AuthenticationSourceContext sourceContext) {
        this.ioc = ioc;
        this.securityManager = securityManager;
        this.sourceContext = sourceContext;
    }

    @Override
    public Object intercept(InvocationContext<Object, Object> context) {

        Collection<AuthenticationSource> sources = new ArrayList<>();

        context.options(Options.class).ifPresent(options -> {
            for (var souceClass : options.sources()) {
                sources.add(ioc.instance(souceClass));
            }

            if (options.login()) {
                securityManager.login(sources);
            } else {
                sourceContext.setSources(sources);
            }

        });

        return null;
    }
}
