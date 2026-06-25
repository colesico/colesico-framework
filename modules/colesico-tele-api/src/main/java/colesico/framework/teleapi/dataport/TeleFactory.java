package colesico.framework.teleapi.dataport;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;

import colesico.framework.teleapi.TeleException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Tele-readers/writers factory
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
    public <R extends TeleReader<?, ?>> R provideReader(Class<R> readerClass) {
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
    @SafeVarargs
    public final <R extends TeleReader<V, O>, V, O extends ReadOptions> R provideReader(Class<V> baseType, Class<? extends R>... readerBaseClasses) {
        var reader = findReader(baseType, readerBaseClasses);
        if (reader != null) {
            return reader;
        }
        throw new TeleException("Unable to get tele-reader for " + baseType);
    }

    /**
     * Finds appropriate reader for given base class and the type that to be read.
     * Returns null if reader not found
     */
    @SafeVarargs
    public final <R extends TeleReader<V, O>, V, O extends ReadOptions> R findReader(Class<V> baseType, Class<? extends R>... readerBaseClasses) {
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
    public <W extends TeleWriter<?, ?>> W provideWriter(Class<W> writerClass) {
        return ioc.instance(writerClass);
    }

    /**
     * Returns appropriate writer for given base class and the type that to be written.
     * Throws an exception if reader not found
     */
    @SafeVarargs
    public final <W extends TeleWriter<V, O>, V, O extends WriteOptions> W provideWriter(Class<V> baseType, Class<? extends W>... writerBaseClasses) {
        var writer = findWriter(baseType, writerBaseClasses);
        if (writer != null) {
            return writer;
        }
        throw new TeleException("Unable to get tele-writer for " + baseType);
    }

    @SafeVarargs
    public final <W extends TeleWriter<V, O>, V, O extends WriteOptions> W findWriter(Class<V> baseType, Class<? extends W>... writerBaseClasses) {
        for (var writerBaseClass : writerBaseClasses) {
            var reader = ioc.instanceOrNull(new ClassedKey<>(writerBaseClass, baseType));
            if (reader != null) {
                return reader;
            }
        }
        return null;
    }

}
