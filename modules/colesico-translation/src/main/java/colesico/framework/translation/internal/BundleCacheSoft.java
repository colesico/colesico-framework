package colesico.framework.translation.internal;

import colesico.framework.translation.Bundle;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class BundleCacheSoft implements BundleCache {

    private final ConcurrentMap<Key, SoftReference<Bundle>> dictionaryMap = new ConcurrentHashMap<>();
    private final ReferenceQueue<? super Bundle> referenceQueue = new ReferenceQueue<>();
    private final double cleanUpFrequency;

    public BundleCacheSoft(double cleanUpFrequency) {
        this.cleanUpFrequency = cleanUpFrequency;
    }

    @Override
    public Bundle get(Key key) {
        SoftReference<Bundle> ref = dictionaryMap.get(key);
        if (ref == null) {
            return null;
        }

        return ref.get();
    }

    @Override
    public void set(Key key, Bundle dictionary) {
        if (canCleanup()) {
            cleanup();
        }
        DictionaryReference ref = new DictionaryReference(dictionary, referenceQueue, key);
        dictionaryMap.put(key, ref);
    }

    @Override
    public void cleanup() {
        DictionaryReference ref;
        while ((ref = (DictionaryReference) referenceQueue.poll()) != null) {
            dictionaryMap.remove(ref.getKey());
        }
    }

    @Override
    public void invalidate() {
        cleanup();
        dictionaryMap.clear();
    }

    private boolean canCleanup() {
        return Math.random() < cleanUpFrequency;
    }

    private static class DictionaryReference extends SoftReference<Bundle> {
        private final Key key;

        public DictionaryReference(Bundle referent, ReferenceQueue<? super Bundle> queue, Key key) {
            super(referent, queue);
            this.key = key;
        }

        public Key getKey() {
            return key;
        }
    }
}
