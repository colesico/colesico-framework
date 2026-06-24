package colesico.framework.teleapi.dataport;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;

import colesico.framework.teleapi.TeleException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Tele-readers and writers factory
 */
@Singleton
public final class TeleFactory {

    private final Ioc ioc;

    @Inject
    public TeleFactory(Ioc ioc) {
        this.ioc = ioc;
    }

    /**
     * Returns reader by its exact class
     * Throws an exception if reader not found  in the IoC context
     */
    public <T extends TeleReader<?, ?>> T reader(Class<T> readerClass) {
        var reader = ioc.instanceOrNull(readerClass);
        if (reader != null) {
            return reader;
        }
        throw new TeleException("Unable to find tele reader: " + readerClass);
    }

    /**
     * Returns appropriate reader for given base class and the type that to be read.
     * Throws an exception if reader not found
     */
    public <T extends TeleReader<V, O>, V, O extends ReadOptions> T reader(Class<V> baseType, Class<? extends T>... readerBaseClasses) {
        var reader = findReader(baseType, readerBaseClasses);
        if (reader != null) {
            return (T) reader;
        }
        throw new TeleException("Unable to get tele reader for " + baseType);
    }

    /**
     * Finds appropriate reader for given base class and the type that to be read.
     * Returns null if reader not found
     */
    public <T extends TeleReader<V, O>, V, O extends ReadOptions> T findReader(Class<V> baseType, Class<? extends T>... readerBaseClasses) {
        for (var readerBaseClass : readerBaseClasses) {
            var reader = ioc.instanceOrNull(new ClassedKey<>(readerBaseClass, baseType));
            if (reader != null) {
                return reader;
            }
        }
        return null;
    }

    /**
     * Returns writer by its exact class
     */
    public <W extends TeleWriter<?, ?>> W writer(Class<W> writerClass) {
        return ioc.instance(writerClass);
    }

    /**
     * Returns appropriate writer for given base class and the type that to be written.
     * Throws an exception if reader not found
     */
    public <W extends TeleWriter<?, ?>, V> W writer(Class<W> writerBaseClass, Class<V> baseType) {
        return ioc.instance(new ClassedKey<>(writerBaseClass, baseType));
    }

    public <W extends TeleWriter<?, ?>, V> W findWriter(Class<W> writerBaseClass, Class<V> baseType) {
        return ioc.instanceOrNull(new ClassedKey<>(writerBaseClass, baseType));
    }

}
