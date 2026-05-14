package colesico.framework.security.internal;

import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.security.IdentityContext;

public class IdentityContextImpl implements IdentityContext {

    protected final RequestScope requestScope;

    public IdentityContextImpl(RequestScope requestScope) {
        this.requestScope = requestScope;
    }

    @Override
    public Entry identity() {
        return requestScope.get(Entry.SCOPE_KEY);
    }

    @Override
    public void setEntry(Entry entry) {
        requestScope.put(Entry.SCOPE_KEY, entry);
    }

    @Override
    public void clear() {
        requestScope.put(Entry.SCOPE_KEY, null);
    }

}
