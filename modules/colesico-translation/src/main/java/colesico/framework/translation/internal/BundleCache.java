package colesico.framework.translation.internal;

import colesico.framework.translation.Bundle;

public interface BundleCache {

    Bundle get(Key key);

    void set(Key key, Bundle dictionary);

    void cleanup();

    void invalidate();

    final class Key {
        /**
         * Resource path to bundle file  (.properties) without extension
         */
        private final String actualPath;

        public Key(String actualPath) {
            this.actualPath = actualPath;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key cacheKey = (Key) o;

            return actualPath.equals(cacheKey.actualPath);
        }

        @Override
        public int hashCode() {
            return actualPath.hashCode();
        }
    }
}
