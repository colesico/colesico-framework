package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Represents a generic options for writing data to channel with {@link TeleWriter}
 */
public interface WriteOptions<W extends TeleWriter<?, ?>> {

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
    default Class<? extends W> customWriter() {
        return null;
    }

    default <M> M metadataAs(Class<M> type) {
        return type.cast(metadata());
    }
}
