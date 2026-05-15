package colesico.framework.security.internal;

import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;

public class IdentityContextImpl implements IdentityContext {

    protected final RequestScope requestScope;

    public IdentityContextImpl(RequestScope requestScope) {
        this.requestScope = requestScope;
    }

    @Override
    public Identity<?> identity() {
        return requestScope.get(SCOPE_KEY);
    }

    @Override
    public void setIdentity(Identity<?> identity) {
        requestScope.put(SCOPE_KEY, identity);
    }

    @Override
    public void clear() {
        requestScope.put(SCOPE_KEY, null);
    }

}
