package colesico.framework.profile.assist;

import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.profile.AbstractProfileManager;
import colesico.framework.profile.Profile;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class SimpleProfileManager extends AbstractProfileManager<Profile> {

    private AtomicReference<Profile> profileHolder;

    @Inject
    public SimpleProfileManager(RequestScope requestScope) {
        super(requestScope);
        this.profileHolder = new AtomicReference<>(createProfile());
    }

    @Override
    protected Profile createProfile() {
        return new Profile.Default(Locale.getDefault());
    }

    @Override
    protected Profile read(Profile profile) {
        return profileHolder.get();
    }

    @Override
    protected Profile write(Profile profile) {
        profileHolder.set(profile);
        return profile;
    }
}
