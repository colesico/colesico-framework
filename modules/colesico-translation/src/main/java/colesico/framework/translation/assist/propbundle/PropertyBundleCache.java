package colesico.framework.translation.assist.propbundle;

import colesico.framework.resource.l10n.ObjectiveQualifiers;

import java.util.Objects;

public interface PropertyBundleCache {

    PropertyBundle get(Key key);

    void set(Key key, PropertyBundle bundle);

    void cleanup();

    void invalidate();

    final class Key {

        private final String baseName;
        private final ObjectiveQualifiers qualifiers;

        public Key(String baseName, ObjectiveQualifiers qualifiers) {
            this.baseName = baseName;
            this.qualifiers = qualifiers;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "baseName='" + baseName + '\'' +
                    ", qualifiers=" + qualifiers +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return baseName.equals(key.baseName) &&
                    qualifiers.equals(key.qualifiers);
        }

        @Override
        public int hashCode() {
            return Objects.hash(baseName, qualifiers);
        }
    }
}
