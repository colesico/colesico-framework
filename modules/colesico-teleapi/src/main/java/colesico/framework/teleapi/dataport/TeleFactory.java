package colesico.framework.teleapi.dataport;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;

import colesico.framework.teleapi.TeleException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.lang.reflect.Type;

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
     * Returns reader by its exact class.
     * Throws an exception if reader not found in the IoC context.
     */
    public <R extends TeleReader<?, ?>> R reader(Class<R> readerClass) {
        var reader = ioc.instanceOrNull(readerClass);
        if (reader != null) {
            return reader;
        }
        throw new TeleException("Unable to find tele reader: " + readerClass);
    }

    /**
     * Returns appropriate reader for given base type and reader base classes.
     * Throws an exception if reader not found.
     */
    @SafeVarargs
    public final <R extends TeleReader<?, ?>> R provideReader(Type baseType, Class<? extends TeleReader>... readerBaseClasses) {
        var reader = findReader(baseType, readerBaseClasses);
        if (reader != null) {
            return (R) reader;
        }
        throw new TeleException("Unable to get tele-reader for " + baseType);
    }

    /**
     * Finds appropriate reader for given base type and reader base classes.
     * Returns null if reader not found.
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <R extends TeleReader<?, ?>> R findReader(Type baseType, Class<? extends TeleReader>... readerBaseClasses) {
        for (var readerBaseClass : readerBaseClasses) {
            var reader = ioc.instanceOrNull(new ClassedKey<>(readerBaseClass, baseType));
            if (reader != null) {
                return (R) reader;
            }
        }
        return null;
    }

    /**
     * Returns writer by its exact class.
     */
    public <W extends TeleWriter<?, ?>> W writer(Class<W> writerClass) {
        return ioc.instance(writerClass);
    }

    /**
     * Returns appropriate writer for given base type and writer base classes.
     * Throws an exception if writer not found.
     */
    @SafeVarargs
    public final <W extends TeleWriter<?, ?>> W provideWriter(Type baseType, Class<? extends TeleWriter>... writerBaseClasses) {
        var writer = findWriter(baseType, writerBaseClasses);
        if (writer != null) {
            return (W) writer;
        }
        throw new TeleException("Unable to get tele-writer for " + baseType);
    }

    /**
     * Finds appropriate writer for given base type and writer base classes.
     * Returns null if writer not found.
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <W extends TeleWriter<?, ?>> W findWriter(Type baseType, Class<? extends TeleWriter>... writerBaseClasses) {
        if (baseType == null){
            return null;
        }
        for (var writerBaseClass : writerBaseClasses) {
            var writer = ioc.instanceOrNull(new ClassedKey<>(writerBaseClass, baseType));
            if (writer != null) {
                return (W) writer;
            }
        }
        return null;
    }

}