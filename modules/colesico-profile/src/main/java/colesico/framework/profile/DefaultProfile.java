package colesico.framework.profile;

import java.util.Locale;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "DefaultProfile{" +
                "locale=" + locale +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DefaultProfile that = (DefaultProfile) o;
        return Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(locale);
    }
}
