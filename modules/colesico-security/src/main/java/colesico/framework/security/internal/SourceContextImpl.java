package colesico.framework.security.internal;

import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.security.authentication.AuthenticationSource;
import colesico.framework.security.authentication.SourceContext;

public class SourceContextImpl implements SourceContext {

    protected final RequestScope requestScope;

    public SourceContextImpl(RequestScope requestScope) {
        this.requestScope = requestScope;
    }

    @Override
    public Iterable<AuthenticationSource> sources() {
        var sources = requestScope.get(SCOPE_KEY);
        return sources == null ? null : sources.items();
    }

    @Override
    public void setSources(Iterable<AuthenticationSource> sources) {
        requestScope.put(SCOPE_KEY, new Sources(sources));
    }

    @Override
    public void clear() {
        requestScope.put(SCOPE_KEY, null);
    }

}
