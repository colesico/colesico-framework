package colesico.framework.security.internal;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;

public class IdentityContextImpl implements IdentityContext {

    protected final ThreadScope threadScope;

    public IdentityContextImpl(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    @Override
    public Holder holder() {
        return threadScope.get(Holder.SCOPE_KEY);
    }

    @Override
    public void setIdentity(Identity<?> identity) {
        threadScope.put(Holder.SCOPE_KEY, new Holder(identity));
    }

    @Override
    public void clear() {
        threadScope.put(Holder.SCOPE_KEY, null);
    }
}
