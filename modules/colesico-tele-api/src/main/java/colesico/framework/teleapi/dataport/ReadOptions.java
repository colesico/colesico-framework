package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Represents a generic options for reading data from channel with {@link TeleReader}
 */
public interface ReadOptions<R extends TeleReader<?, ?>> {

    /**
     * Reading value base type
     */
    Type baseType();

    /**
     * Returns the custom data attached to these options
     */
    Object metadata();

    /**
     * Overrides the default reader to be used for reading the value
     */
    default Class<? extends R> customReader() {
        return null;
    }


    default <M> M metadataAs(Class<M> type) {
        return type.cast(metadata());
    }
}
