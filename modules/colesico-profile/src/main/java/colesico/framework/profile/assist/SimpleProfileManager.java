package colesico.framework.profile.assist;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.profile.Profile;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class SimpleProfileManager extends AbstractProfileManager<Profile> {

    private final AtomicReference<Profile> profileHolder;

    @Inject
    public SimpleProfileManager(TaskScope taskScope) {
        super(taskScope);
        this.profileHolder = new AtomicReference<>(createProfile());
    }

    @Override
    protected Profile createProfile() {
        return new Profile.Default(Locale.getDefault());
    }

    @Override
    protected Profile read() {
        return profileHolder.get();
    }

    @Override
    protected void write(Profile profile) {
        profileHolder.set(profile);
    }

}
