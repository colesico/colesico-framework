package colesico.framework.profile.internal;

import colesico.framework.config.Config;
import colesico.framework.profile.ProfileConfigPrototype;

import java.util.Locale;

@Config
public class DefaultPropertyConfig extends ProfileConfigPrototype {

    @Override
    public PropertyConverterBinding[] getConverterBindings() {
        return new PropertyConverterBinding[]{
                new PropertyConverterBinding<>(Locale.class, LocaleConverter.of())
        };
    }
}
