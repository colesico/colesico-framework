package colesico.framework.profile.internal;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileContext;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * Profile context based on {@link TaskScope}
 */
@Singleton
public class ProfileContextImpl implements ProfileContext {

    protected final TaskScope taskScope;

    public ProfileContextImpl(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends Profile<?>> Optional<P> profile() {
        return Optional.ofNullable((P) taskScope.get(SCOPE_KEY));
    }

    @Override
    public void setProfile(Profile<?> profile) {
        taskScope.put(SCOPE_KEY, profile);
    }

    @Override
    public void clear() {
        taskScope.remove(SCOPE_KEY);
    }
}