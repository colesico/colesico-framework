package colesico.framework.security.internal;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.security.authentication.AuthenticationSource;
import colesico.framework.security.authentication.SourceContext;

public class SourceContextImpl implements SourceContext {

    protected final TaskScope taskScope;

    public SourceContextImpl(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    @Override
    public Iterable<AuthenticationSource> sources() {
        var sources = taskScope.get(SCOPE_KEY);
        return sources == null ? null : sources.items();
    }

    @Override
    public void setSources(Iterable<AuthenticationSource> sources) {
        taskScope.put(SCOPE_KEY, new Sources(sources));
    }

    @Override
    public void clear() {
        taskScope.put(SCOPE_KEY, null);
    }

}
