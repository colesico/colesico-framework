package colesico.framework.profile;

import java.util.Locale;
import java.util.Objects;

/**
 * Profile basic implementation
 */
abstract public class AbstractProfile implements Profile {

    protected Locale locale;

    public AbstractProfile() {
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "locale=" + locale +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractProfile that = (AbstractProfile) o;
        return Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(locale);
    }
}
