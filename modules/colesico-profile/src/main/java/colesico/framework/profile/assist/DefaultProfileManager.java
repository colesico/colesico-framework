package colesico.framework.profile.assist;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.profile.AbstractProfileManager;
import colesico.framework.profile.Profile;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class DefaultProfileManager<P extends Profile> extends AbstractProfileManager<P> {

    private final AtomicReference<P> profileHolder;

    @Inject
    public DefaultProfileManager(TaskScope taskScope) {
        super(taskScope);
        this.profileHolder = new AtomicReference<>(createProfile());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected P createProfile() {
        return (P) new Profile.Default(Locale.getDefault());
    }

    @Override
    protected P read() {
        return profileHolder.get();
    }

    @Override
    protected P write(P profile) {
        profileHolder.set(profile);
        return profile;
    }
}
