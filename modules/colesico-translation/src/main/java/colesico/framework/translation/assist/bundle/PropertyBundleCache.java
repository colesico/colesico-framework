package colesico.framework.translation.assist.bundle;

import java.util.Locale;
import java.util.Objects;

public interface PropertyBundleCache {

    PropertyBundle get(Key key);

    void set(Key key, PropertyBundle bundle);

    void cleanup();

    void invalidate();

    final class Key {

        private final String baseName;
        private final Locale locale;

        public Key(String baseName, Locale locale) {
            this.baseName = baseName;
            this.locale = locale;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return baseName.equals(key.baseName) &&
                    locale.equals(key.locale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(baseName, locale);
        }
    }
}
