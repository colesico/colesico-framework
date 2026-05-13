package colesico.framework.security.internal;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.security.IdentityContext;

public class IdentityContextImpl implements IdentityContext {

    protected final ThreadScope threadScope;

    public IdentityContextImpl(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    @Override
    public Entry get() {
        return threadScope.get(Entry.SCOPE_KEY);
    }

    @Override
    public void setEntry(Entry entry) {
        threadScope.put(Entry.SCOPE_KEY, entry);
    }

    @Override
    public void clear() {
        threadScope.put(Entry.SCOPE_KEY, null);
    }

}
