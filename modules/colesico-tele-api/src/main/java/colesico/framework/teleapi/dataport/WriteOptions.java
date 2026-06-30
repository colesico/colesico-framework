package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Represents a generic options for writing data to channel with {@link TeleWriter}
 */
public interface WriteOptions {

    /**
     * Writing value base type
     */
    Type baseType();

    /**
     * Returns the custom data attached to these options.
     */
    Object metadata();

    /**
     * Overrides the default writer to be used for writing the value
     */
    default Class<? extends TeleWriter<?, ?>> customWriter() {
        return null;
    }

    default <T> T metadataAs(Class<T> type) {
        return type.cast(metadata());
    }
}
