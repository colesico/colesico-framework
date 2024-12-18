package colesico.framework.profile.internal;

import colesico.framework.config.Config;
import colesico.framework.profile.ProfileConverterBindings;

import java.util.Locale;

@Config
public class LocaleConverterBinding extends ProfileConverterBindings {

    @Override
    public PropertyConverterBinding[] getConverterBindings() {
        return new PropertyConverterBinding[]{
                new PropertyConverterBinding<>(Locale.class, LocaleConverter.of())
        };
    }
}
