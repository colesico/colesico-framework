package colesico.framework.translation.assist.propbundle;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class PropertyBundleCacheSoft implements PropertyBundleCache {

    private final ConcurrentMap<Key, BundleReference> bundleMap = new ConcurrentHashMap<>();
    private final ReferenceQueue referenceQueue = new ReferenceQueue<>();
    private final double cleanUpFrequency;

    public PropertyBundleCacheSoft(double cleanUpFrequency) {
        this.cleanUpFrequency = cleanUpFrequency;
    }

    @Override
    public PropertyBundle get(Key key) {
        SoftReference<PropertyBundle> ref = bundleMap.get(key);
        if (ref == null) {
            return null;
        }

        return ref.get();
    }

    @Override
    public void set(Key key, PropertyBundle bundle) {
        if (canCleanup()) {
            cleanup();
        }
        BundleReference ref = new BundleReference(bundle, referenceQueue, key);
        bundleMap.put(key, ref);
    }

    @Override
    public void cleanup() {
        BundleReference ref;
        while ((ref = (BundleReference) referenceQueue.poll()) != null) {
            bundleMap.remove(ref.getKey());
        }
    }

    @Override
    public void invalidate() {
        cleanup();
        bundleMap.clear();
    }

    private boolean canCleanup() {
        return Math.random() < cleanUpFrequency;
    }

    private static class BundleReference extends SoftReference<PropertyBundle> {

        private final Key key;

        public BundleReference(PropertyBundle referent, ReferenceQueue<? super PropertyBundle> queue, Key key) {
            super(referent, queue);
            this.key = key;
        }

        public Key getKey() {
            return key;
        }
    }
}
