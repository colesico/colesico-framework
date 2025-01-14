package colesico.framework.profile;

import java.util.Locale;

/**
 * Profile default implementation
 */
public class DefaultProfile implements Profile {

    protected Locale locale;

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
