package colesico.framework.teleapi.dataport;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;

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
    public <R extends TeleReader<?, ?>> R reader(Class<R> readerClass) {
        return ioc.instance(readerClass);
    }

    /**
     * Returns appropriate reader for given base class and the type that to be read.
     * Throws an exception if reader not found
     */
    public <R extends TeleReader<?, ?>, V> R reader(Class<R> readerBaseClass, Class<V> valueType) {
        return ioc.instance(new ClassedKey<>(readerBaseClass, valueType));
    }

    /**
     * Finds appropriate reader for given base class and the type that to be read.
     * Returns null if reader not found
     */
    public <R extends TeleReader<?, ?>, V> R findReader(Class<R> readerBaseClass, Class<V> baseType) {
        return ioc.instanceOrNull(new ClassedKey<>(readerBaseClass, baseType));
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
