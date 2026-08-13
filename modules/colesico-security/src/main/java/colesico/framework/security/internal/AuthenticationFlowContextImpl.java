package colesico.framework.security.internal;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.security.authentication.AuthenticationsContext;
import colesico.framework.security.authentication.AuthenticationSource;

public class AuthenticationFlowContextImpl implements AuthenticationsContext {

    protected final TaskScope taskScope;

    public AuthenticationFlowContextImpl(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    @Override
    public Iterable<AuthenticationSource<?,?>> authentications() {
        var sources = taskScope.get(SCOPE_KEY);
        return sources == null ? null : sources.items();
    }

    @Override
    public void setFlows(Iterable<AuthenticationSource<?,?>> sources) {
        taskScope.put(SCOPE_KEY, new Authentications(sources));
    }

    @Override
    public void clear() {
        taskScope.put(SCOPE_KEY, null);
    }

}
