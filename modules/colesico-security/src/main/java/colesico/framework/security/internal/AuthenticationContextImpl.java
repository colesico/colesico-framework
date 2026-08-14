package colesico.framework.security.internal;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.Authenticator;

public class AuthenticationContextImpl implements AuthenticationContext {

    protected final TaskScope taskScope;

    public AuthenticationContextImpl(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    @Override
    public Iterable<Authenticator<?,?>> authenticators() {
        var authenticators = taskScope.get(SCOPE_KEY);
        return authenticators == null ? null : authenticators.items();
    }

    @Override
    public void setAuthenticators(Iterable<Authenticator<?,?>> authenticators) {
        taskScope.put(SCOPE_KEY, new Authenticators(authenticators));
    }

    @Override
    public void clear() {
        taskScope.put(SCOPE_KEY, null);
    }

}
