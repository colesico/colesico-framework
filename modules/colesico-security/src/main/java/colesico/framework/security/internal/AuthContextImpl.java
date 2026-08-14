package colesico.framework.security.internal;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.security.authentication.AuthContext;
import colesico.framework.security.authentication.Authentication;

public class AuthContextImpl implements AuthContext {

    protected final TaskScope taskScope;

    public AuthContextImpl(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    @Override
    public Iterable<Authentication<?,?>> authentications() {
        var authentications = taskScope.get(SCOPE_KEY);
        return authentications == null ? null : authentications.items();
    }

    @Override
    public void setAuthentications(Iterable<Authentication<?,?>> authentications) {
        taskScope.put(SCOPE_KEY, new Authentications(authentications));
    }

    @Override
    public void clear() {
        taskScope.put(SCOPE_KEY, null);
    }

}
