package colesico.framework.profile;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

import java.util.Locale;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class ProfileConfigPrototype {
    abstract public Locale getDefaultLocale();
}
