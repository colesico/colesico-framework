package colesico.framework.profile.internal;

import colesico.framework.profile.ProfileConfigPrototype;

import java.util.Locale;

public class DefaultProfileConfig extends ProfileConfigPrototype {
    @Override
    public Locale getDefaultLocale() {
        return Locale.getDefault();
    }
}
