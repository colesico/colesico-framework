package colesico.framework.security.internal;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;

import java.util.Optional;

public class IdentityContextImpl implements IdentityContext {

    protected final TaskScope taskScope;

    public IdentityContextImpl(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    @Override
    public Optional<Identity> identity() {
        return Optional.ofNullable(taskScope.get(SCOPE_KEY));
    }

    @Override
    public void setIdentity(Identity identity) {
        taskScope.put(SCOPE_KEY, identity);
    }

    @Override
    public void clear() {
        taskScope.remove(SCOPE_KEY);
    }

}
