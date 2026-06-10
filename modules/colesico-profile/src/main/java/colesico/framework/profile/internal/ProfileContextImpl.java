package colesico.framework.profile.internal;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileContext;
import jakarta.inject.Singleton;

@Singleton
public class ProfileContextImpl implements ProfileContext {

    protected final TaskScope taskScope;

    public ProfileContextImpl(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    @Override
    public Profile profile() {
        return taskScope.get(SCOPE_KEY);
    }

    @Override
    public void setProfile(Profile profile) {
        taskScope.put(SCOPE_KEY, profile);
    }

    @Override
    public void clear() {
        taskScope.remove(SCOPE_KEY);
    }
}
