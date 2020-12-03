package colesico.framework.teleapi;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Type;

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
     */
    public <R extends TeleReader> R getReader(Class<R> readerClass) {
        return ioc.instance(readerClass);
    }

    /**
     * Returns appropriate reader for given base class and the type that to be read.
     * Throws an exception if reader not found
     */
    public <R extends TeleReader> R getReader(Class<R> readerBaseClass, Type valueType) {
        return ioc.instance(new ClassedKey<>(readerBaseClass.getCanonicalName(), typeToClassName(valueType)), null);
    }

    /**
     * Finds appropriate reader for given base class and the type that to be read.
     * Returns null if reader not found
     */
    public <R extends TeleReader> R findReader(Class<R> readerBaseClass, Type valueType) {
        return ioc.instanceOrNull(new ClassedKey<>(readerBaseClass.getCanonicalName(), typeToClassName(valueType)), null);
    }

    /**
     * Returns writer by its exact class
     */
    public <W extends TeleWriter> W getWriter(Class<W> writerClass) {
        return ioc.instance(writerClass);
    }

    /**
     * Returns appropriate writer for given base class and the type that to be write.
     * Throws an exception if reader not found
     */
    public <W extends TeleWriter> W getWriter(Class<W> writerBaseClass, Type valueType) {
        return ioc.instance(new ClassedKey<>(writerBaseClass.getCanonicalName(), typeToClassName(valueType)), null);
    }

    public <W extends TeleWriter> W findWriter(Class<W> writerBaseClass, Type valueType) {
        return ioc.instanceOrNull(new ClassedKey<>(writerBaseClass.getCanonicalName(), typeToClassName(valueType)), null);
    }

    private String typeToClassName(Type valueType) {
        if (valueType instanceof Class) {
            return ((Class) valueType).getCanonicalName();
        } else {
            return valueType.getTypeName();
        }
    }
}
